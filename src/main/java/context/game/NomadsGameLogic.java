package context.game;

import static context.game.visuals.GameCamera.RENDER_RADIUS;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import common.event.GameEvent;
import common.math.Vector2i;
import context.game.logic.QueueProcessor;
import context.game.logic.handler.*;
import context.game.visuals.GameCamera;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.game.logicprocessing.CardPlayedEvent;
import event.network.game.CardPlayedNetworkEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import model.actor.CardPlayer;
import model.state.GameState;
import networking.GameNetwork;
import networking.NetworkEventDispatcher;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;
	private GameCamera camera = new GameCamera();

	private Queue<GameEvent> networkSync = new ArrayBlockingQueue<>(100);
	private Queue<GameEvent> visualSync = new ArrayBlockingQueue<>(100);
	private QueueProcessor queueProcessor;

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

		CardResolvedEventHandler cardResolvedEventHandler = new CardResolvedEventHandler(data, networkSync, visualSync);
		CardPlayedEventHandler cpeHandler = new CardPlayedEventHandler(data, cardResolvedEventHandler);

		queueProcessor = new QueueProcessor(cardResolvedEventHandler);
		addHandler(CardPlayedEvent.class, new CardPlayedEventValidationTest(data), new DoNothingConsumer<>(), true);
		addHandler(CardPlayedEvent.class, cpeHandler);
		addHandler(CardPlayedEvent.class, new CardPlayedEventVisualSyncHandler(data, visualSync));
		addHandler(CardPlayedEvent.class, new CardPlayedEventNetworkSyncHandler(networkSync));

		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventHandler(data, cpeHandler));
		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventVisualSyncHandler(data, visualSync));

		addHandler(PeerConnectRequestEvent.class,
				new InGamePeerConnectRequestEventHandler(networkSync, visualSync, nonce, username));
//		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(sync)); 
//		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
	}

	@Override
	public void update() {
		GameState nextState = data.nextState();
		queueProcessor.processAll(nextState);
		dispatcher.dispatch(networkSync);

		for (int y = -RENDER_RADIUS; y <= RENDER_RADIUS; y++) {
			for (int x = -RENDER_RADIUS; x <= RENDER_RADIUS; x++) {
				Vector2i chunkPos = camera.chunkPos().add(x, y);
				if (nextState.worldMap().chunk(chunkPos) == null) {
					nextState.worldMap().addChunk(nextState.worldMap().generateChunk(chunkPos));
					List<CardPlayer> generateActors = nextState.worldMap().generateActors(chunkPos, nextState);
					for (CardPlayer actor : generateActors) {
						actor.displayer().doInit(context().resourcePack());
						nextState.add(actor);
					}
				}
			}
		}

		nextState.chainHeap().processAll(data, visualSync);
		nextState.actors().forEach(a -> a.update(nextState));
		pushAll(visualSync);

		data.setNextState(nextState.copy());
		data.states().add(nextState);
	}

	public GameCamera camera() {
		return camera;
	}

}
