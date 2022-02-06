package context.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import common.event.GameEvent;
import context.game.logic.QueueProcessor;
import context.game.logic.handler.CardPlayedEventAddToQueueHandler;
import context.game.logic.handler.CardPlayedEventHandler;
import context.game.logic.handler.CardPlayedEventNetworkSyncHandler;
import context.game.logic.handler.CardPlayedEventValidationTest;
import context.game.logic.handler.CardPlayedEventVisualSyncHandler;
import context.game.logic.handler.CardPlayedNetworkEventHandler;
import context.game.logic.handler.CardPlayedNetworkEventVisualSyncHandler;
import context.game.logic.handler.CardResolvedEventHandler;
import context.game.logic.handler.DoNothingConsumer;
import context.game.logic.handler.InGamePeerConnectRequestEventHandler;
import context.game.visuals.GameCamera;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.game.logicprocessing.CardPlayedEvent;
import event.network.game.CardPlayedNetworkEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import model.actor.Actor;
import model.actor.ItemActor;
import model.item.Item;
import model.item.ItemCollection;
import model.state.GameState;
import networking.GameNetwork;
import networking.NetworkEventDispatcher;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;
	private GameCamera camera = new GameCamera();

	private Queue<GameEvent> networkSync = new ArrayBlockingQueue<>(100);
	private Queue<GameEvent> visualSync = new ArrayBlockingQueue<>(100);
	private QueueProcessor queueProcessor;

	private Queue<CardPlayedEvent> cardPlayedEventQueue = new ArrayDeque<>();
	private CardResolvedEventHandler cardResolvedEventHandler;
	private CardPlayedEventHandler cpeHandler;
	private CardPlayedEventVisualSyncHandler cpeVisualSyncHandler;

	private GameNetwork network;
	private NetworkEventDispatcher dispatcher;

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

		cardResolvedEventHandler = new CardResolvedEventHandler(data, networkSync, visualSync);
		cpeHandler = new CardPlayedEventHandler(data, cardResolvedEventHandler);
		cpeVisualSyncHandler = new CardPlayedEventVisualSyncHandler(data, visualSync);

		queueProcessor = new QueueProcessor(cardResolvedEventHandler);

		addHandler(CardPlayedEvent.class, new CardPlayedEventValidationTest(data), new DoNothingConsumer<>(), true);
		CardPlayedEventAddToQueueHandler addToQueueHandler = new CardPlayedEventAddToQueueHandler(cardPlayedEventQueue);
		addHandler(CardPlayedEvent.class, addToQueueHandler);
		addHandler(CardPlayedEvent.class, new CardPlayedEventNetworkSyncHandler(networkSync));

		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventHandler(data, addToQueueHandler));
		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventVisualSyncHandler(data, visualSync));

		addHandler(PeerConnectRequestEvent.class, new InGamePeerConnectRequestEventHandler(networkSync, visualSync, nonce, username));
//		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(sync)); 
//		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
	}

	@Override
	public void update() {
		GameState nextState = data.nextState();
		while (!cardPlayedEventQueue.isEmpty()) {
			CardPlayedEvent e = cardPlayedEventQueue.poll();
			cpeHandler.accept(e);
			cpeVisualSyncHandler.accept(e);
		}
		queueProcessor.processAll(nextState);
		dispatcher.dispatch(networkSync);

		nextState.worldMap().generateTerrainAround(camera.chunkPos(), nextState);

		nextState.chainHeap().processAll(data, visualSync);

		updateActors();

		removeDeadActors();
		pushAll(visualSync);

		data.setNextState(nextState.copy());
		data.states().add(nextState);
	}

	private void updateActors() {
		GameState nextState = data.nextState();
		nextState.actors().values().forEach(actor -> actor.update(nextState));
		nextState.npcs().forEach(npc -> npc.update(nextState, cardPlayedEventQueue));
	}

	private void removeDeadActors() {
		GameState nextState = data.nextState();
		List<Actor> toRemove = new ArrayList<>(0);
		for (Actor a : nextState.actors().values()) {
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
						nextState.add(itemActor);
					}
				}
			}
			nextState.actors().remove(a.id());
		}
	}

	public GameCamera camera() {
		return camera;
	}

}
