package context.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import context.game.logic.QueueProcessor;
import context.game.logic.handler.CardPlayedEventFailTest;
import context.game.logic.handler.CardPlayedEventHandler;
import context.game.logic.handler.CardPlayedNetworkEventHandler;
import context.game.logic.handler.CardResolvedEventHandler;
import context.game.logic.handler.ChainEventHandler;
import context.game.logic.handler.DoNothingConsumer;
import context.game.logic.handler.InGamePeerConnectRequestEventHandler;
import context.game.visuals.GameCamera;
import engine.common.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import engine.common.event.GameEvent;
import event.game.NomadRealmsGameEvent;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import event.network.NomadRealmsNetworkEvent;
import event.network.p2p.game.CardPlayedNetworkEvent;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
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
	private GameCamera camera = new GameCamera();

	private QueueProcessor queueProcessor;

	private Queue<CardPlayedEvent> cardPlayedEventQueue = new ArrayDeque<>();
	private CardPlayedEventHandler cpeHandler;

	private GameNetwork network;
	private NetworkEventDispatcher dispatcher;
	private Queue<NomadRealmsNetworkEvent> outgoingNetworkEvents = new PriorityQueue<>();

	private long nonce;
	private String username;

	public NomadsGameLogic(PacketAddress peerAddress, long nonce, String username) {
		this.nonce = nonce;
		this.username = username;
		network = new GameNetwork();
		network.addPeer(peerAddress);
	}

	@Override
	protected void init() {
		data = (NomadsGameData) context().data();
		dispatcher = new NetworkEventDispatcher(network, context().networkSend());

		CardResolvedEventHandler cardResolvedEventHandler = new CardResolvedEventHandler(data);
		cpeHandler = new CardPlayedEventHandler(data, this, outgoingNetworkEvents);

		queueProcessor = new QueueProcessor(cardResolvedEventHandler);
		addHandler(CardPlayedEvent.class, new CardPlayedEventFailTest(data), new DoNothingConsumer<>(), true);
		addHandler(CardPlayedEvent.class, cpeHandler);
		addHandler(CardResolvedEvent.class, cardResolvedEventHandler);

		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventHandler(data, cardPlayedEventQueue));

		addHandler(PeerConnectRequestEvent.class, new InGamePeerConnectRequestEventHandler(nonce, username, outgoingNetworkEvents));
//		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(sync)); 
//		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
		addHandler(ChainEvent.class, new ChainEventHandler(this, data));
		addHandler(NomadRealmsGameEvent.class, this::pushEventToQueueGroup);
	}

	@Override
	public void update() {
		GameState currentState = data.currentState();
		while (!cardPlayedEventQueue.isEmpty()) {
			handleEvent(cardPlayedEventQueue.poll());
		}
		queueProcessor.processAll(currentState, queueGroup());
		dispatcher.dispatch(outgoingNetworkEvents);

		currentState.worldMap().generateTerrainAround(camera.chunkPos(), currentState);

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

	public GameCamera camera() {
		return camera;
	}

	@Override
	public void handleEvent(GameEvent event) {
		super.handleEvent(event);
	}

}
