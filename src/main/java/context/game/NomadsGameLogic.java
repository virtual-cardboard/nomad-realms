package context.game;

import static model.world.chunk.AbstractTileChunk.chunkPos;
import static model.world.tile.Tile.tileCoords;

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
import context.logic.GameLogic;
import engine.common.event.GameEvent;
import event.NomadRealmsAsyncEvent;
import event.NomadRealmsGameEvent;
import event.logicprocessing.CardPlayedEvent;
import event.logicprocessing.CardResolvedEvent;
import event.logicprocessing.SpawnPlayerAsyncEvent;
import event.logicprocessing.SpawnSelfAsyncEvent;
import event.network.NomadRealmsP2PNetworkEvent;
import event.network.c2s.JoinClusterResponseEvent;
import event.network.p2p.game.CardPlayedNetworkEvent;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.s2c.JoiningPlayerNetworkEvent;
import math.WorldPos;
import model.actor.Actor;
import model.actor.ItemActor;
import model.chain.event.ChainEvent;
import model.item.Item;
import model.item.ItemCollection;
import model.state.GameState;
import networking.GameNetwork;
import networking.NetworkEventDispatcher;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;

	private QueueProcessor queueProcessor;

	private final Queue<CardPlayedEvent> cardPlayedEventQueue = new ArrayDeque<>();

	private GameNetwork network;
	private NetworkEventDispatcher dispatcher;
	private final Queue<NomadRealmsP2PNetworkEvent> outgoingNetworkEvents = new ArrayDeque<>();

	public NomadsGameLogic(long startingTick, JoinClusterResponseEvent joinResponse) {
		setGameTick((int) startingTick);
		System.out.println("Starting tick: " + gameTick() + " Spawn tick: " + joinResponse.spawnTick());
		long spawnPosLong = joinResponse.spawnPos();
		WorldPos spawnPos = new WorldPos(chunkPos(spawnPosLong), tileCoords(spawnPosLong));
		handleEvent(new SpawnSelfAsyncEvent(joinResponse.spawnTick(), spawnPos));
	}

	@Override
	protected void init() {
		network = new GameNetwork();
		data = (NomadsGameData) context().data();
		dispatcher = new NetworkEventDispatcher(network, context().networkSend());

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
//		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(sync)); 
//		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
		addHandler(ChainEvent.class, new ChainEventHandler(this, data));
		addHandler(NomadRealmsGameEvent.class, this::pushEventToQueueGroup);
		addHandler(NomadRealmsAsyncEvent.class, this::pushEventToQueueGroup);
		addHandler(NomadRealmsP2PNetworkEvent.class, e -> System.out.println("Received p2p network event: " + e.getClass().getSimpleName()));
	}

	@Override
	public void update() {
		GameState currentState = data.currentState();
		while (!cardPlayedEventQueue.isEmpty()) {
			handleEvent(cardPlayedEventQueue.poll());
		}
		queueProcessor.processAll(currentState, queueGroup());
		dispatcher.dispatch(outgoingNetworkEvents);

		currentState.worldMap().generateTerrainAround(data.camera().chunkPos(), currentState);

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
