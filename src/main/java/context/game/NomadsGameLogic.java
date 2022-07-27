package context.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import context.game.logic.QueueProcessor;
import context.game.logic.asynchandler.SpawnPlayerAsyncEventHandler;
import context.game.logic.asynchandler.SpawnSelfAsyncEventHandler;
import context.game.logic.handler.CardPlayedEventFailTest;
import context.game.logic.handler.CardPlayedEventHandler;
import context.game.logic.handler.CardPlayedNetworkEventHandler;
import context.game.logic.handler.CardResolvedEventHandler;
import context.game.logic.handler.ChainEventHandler;
import context.game.logic.handler.DoNothingConsumer;
import context.game.logic.handler.InGamePeerConnectRequestEventHandler;
import context.game.logic.handler.JoiningPlayerNetworkEventHandler;
import context.game.logic.handler.StreamChunkDataEventHandler;
import context.game.logic.handler.StreamChunksToJoiningPlayerHandler;
import context.logic.GameLogic;
import engine.common.event.GameEvent;
import engine.common.networking.packet.address.PacketAddress;
import event.NomadRealmsAsyncEvent;
import event.NomadRealmsGameEvent;
import event.logicprocessing.CardPlayedEvent;
import event.logicprocessing.CardResolvedEvent;
import event.logicprocessing.SpawnPlayerAsyncEvent;
import event.logicprocessing.SpawnSelfAsyncEvent;
import event.network.NomadRealmsP2PNetworkEvent;
import event.network.c2s.JoinClusterResponseEvent;
import event.network.p2p.game.CardPlayedNetworkEvent;
import event.network.p2p.game.StreamChunkDataEvent;
import event.network.p2p.peerconnect.PeerConnectConfirmationEvent;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.s2c.JoiningPlayerNetworkEvent;
import math.WorldPos;
import model.actor.Actor;
import model.actor.ItemActor;
import model.chain.event.ChainEvent;
import model.item.Item;
import model.item.ItemCollection;
import model.state.GameState;
import networking.NetworkEventDispatcher;

public class NomadsGameLogic extends GameLogic {

	private final WorldPos spawnPos;
	private NomadsGameData data;

	private QueueProcessor queueProcessor;

	private final Queue<CardPlayedEvent> cardPlayedEventQueue = new ArrayDeque<>();

	private NetworkEventDispatcher dispatcher;
	private final Queue<NomadRealmsP2PNetworkEvent> outgoingNetworkEvents = new ArrayDeque<>();

	public NomadsGameLogic(long startingTick, JoinClusterResponseEvent joinResponse) {
		setGameTick((int) startingTick);

		spawnPos = joinResponse.spawnPos();
		handleEvent(new SpawnSelfAsyncEvent(joinResponse.spawnTick(), joinResponse.spawnPos()));

	}

	@Override
	protected void init() {
		data = (NomadsGameData) context().data();

		for (PacketAddress address : data.network().peers()) {
			PeerConnectConfirmationEvent event = new PeerConnectConfirmationEvent(spawnPos);
			context().networkSend().add(event.toPacketModel(address));
		}
		dispatcher = new NetworkEventDispatcher(data.network(), context().networkSend());

		CardResolvedEventHandler cardResolvedEventHandler = new CardResolvedEventHandler(data);
		CardPlayedEventHandler cpeHandler = new CardPlayedEventHandler(data, this, outgoingNetworkEvents);

		queueProcessor = new QueueProcessor(cardResolvedEventHandler);
		addHandler(SpawnPlayerAsyncEvent.class, new SpawnPlayerAsyncEventHandler(data));
		addHandler(SpawnSelfAsyncEvent.class, new SpawnSelfAsyncEventHandler(data));

		addHandler(CardPlayedEvent.class, new CardPlayedEventFailTest(data), new DoNothingConsumer<>(), true);
		addHandler(CardPlayedEvent.class, cpeHandler);
		addHandler(CardResolvedEvent.class, cardResolvedEventHandler);

		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventHandler(data, cardPlayedEventQueue));

		addHandler(JoiningPlayerNetworkEvent.class, new JoiningPlayerNetworkEventHandler(data, asyncEventQueue(), context().networkSend()));

		addHandler(PeerConnectRequestEvent.class, new InGamePeerConnectRequestEventHandler(data, data.username(), context().networkSend()));
		addHandler(PeerConnectConfirmationEvent.class, new StreamChunksToJoiningPlayerHandler(data, context().networkSend()));

		addHandler(StreamChunkDataEvent.class, new StreamChunkDataEventHandler(data));
//		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(sync));
//		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
		addHandler(ChainEvent.class, new ChainEventHandler(this, data));
		addHandler(NomadRealmsGameEvent.class, this::pushEventToQueueGroup);
		addHandler(NomadRealmsAsyncEvent.class, this::pushEventToQueueGroup);
		addHandler(NomadRealmsP2PNetworkEvent.class, e -> data.tools().logMessage("Received p2p network event: " + e.getClass().getSimpleName()));
	}

	@Override
	public void update() {
		GameState currentState = data.currentState();
		while (!cardPlayedEventQueue.isEmpty()) {
			handleEvent(cardPlayedEventQueue.poll());
		}
		queueProcessor.processAll(currentState, queueGroup());
		dispatcher.dispatch(outgoingNetworkEvents);

		currentState.worldMap().generateTerrainAround(data.camera().chunkPos(), currentState, data.generators().npcIdGenerator());

		List<ChainEvent> resolvedChainEvents = currentState.chainHeap().processAll(gameTick(), data, queueGroup());
		resolvedChainEvents.forEach(this::handleEvent);

		updateActors();

		removeDeadActors();

		data.finishCurrentState();
	}

	private void updateActors() {
		GameState currentState = data.currentState();
		currentState.actors().values().forEach(actor -> actor.update(gameTick(), currentState));
		currentState.npcs().forEach(npc -> npc.update(gameTick(), currentState, cardPlayedEventQueue));
	}

	private void removeDeadActors() {
		GameState currentState = data.currentState();
		List<Actor> toRemove = new ArrayList<>(0);
		for (Actor a : currentState.actors().values()) {
			if (a.shouldRemove()) {
				toRemove.add(a);
			}
		}
		for (Actor a : toRemove) {
			ItemCollection items = a.dropItems();
			if (items != null) {
				for (Item item : items.keySet()) {
					for (int i = 0; i < items.get(item); i++) {
						ItemActor itemActor = new ItemActor(item);
						itemActor.setId(data.generators().genId());
						itemActor.worldPos().set(a.worldPos());
						currentState.add(itemActor);
					}
				}
			}
			currentState.actors().remove(a.id().toLongID());
		}
	}

	/**
	 * Increased visibility (public)
	 *
	 * @param event the event to handle
	 * @see GameLogic#handleEvent(GameEvent)
	 */
	@Override
	public void handleEvent(GameEvent event) {
		super.handleEvent(event);
	}

}
