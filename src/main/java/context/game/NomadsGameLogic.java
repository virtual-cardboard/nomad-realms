package context.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import common.event.GameEvent;
import context.game.logic.QueueProcessor;
import context.game.logic.handler.*;
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

	private QueueProcessor queueProcessor;

	private Queue<CardPlayedEvent> cardPlayedEventQueue = new ArrayDeque<>();
	private CardResolvedEventHandler cardResolvedEventHandler;
	private CardPlayedEventHandler cpeHandler;

	private GameNetwork network;
	private NetworkEventDispatcher dispatcher;
	private Queue<GameEvent> toDispatch = new PriorityQueue<>();

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

		cardResolvedEventHandler = new CardResolvedEventHandler(data);
		cpeHandler = new CardPlayedEventHandler(data, cardResolvedEventHandler);

		queueProcessor = new QueueProcessor(cardResolvedEventHandler);

		addHandler(CardPlayedEvent.class, new CardPlayedEventFailTest(data), new DoNothingConsumer<>(), true);
		CardPlayedEventAddToQueueHandler addToQueueHandler = new CardPlayedEventAddToQueueHandler(cardPlayedEventQueue);
		addHandler(CardPlayedEvent.class, addToQueueHandler);

		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventHandler(data, addToQueueHandler));

		addHandler(PeerConnectRequestEvent.class, new InGamePeerConnectRequestEventHandler(nonce, username));
//		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(sync)); 
//		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
		addHandler(GameEvent.class, this::pushEvent);
	}

	@Override
	public void update() {
		GameState currentState = data.currentState();
		while (!cardPlayedEventQueue.isEmpty()) {
			CardPlayedEvent e = cardPlayedEventQueue.poll();
			cpeHandler.accept(e);
		}
		queueProcessor.processAll(currentState);
		dispatcher.dispatch(toDispatch);

		currentState.worldMap().generateTerrainAround(camera.chunkPos(), currentState);

		currentState.chainHeap().processAll(gameTick(), data, visualSync);

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

}
