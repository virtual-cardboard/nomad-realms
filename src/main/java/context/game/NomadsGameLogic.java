package context.game;

import static context.game.visuals.GameCamera.RENDER_RADIUS;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import common.event.GameEvent;
import common.math.Vector2i;
import context.game.logic.QueueProcessor;
import context.game.logic.handler.CardPlayedEventHandler;
import context.game.logic.handler.CardPlayedEventNetworkSyncHandler;
import context.game.logic.handler.CardPlayedEventValidator;
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
import model.GameState;
import model.actor.CardPlayer;
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
		CardPlayedEventHandler cpeHandler = new CardPlayedEventHandler(cardResolvedEventHandler);

		queueProcessor = new QueueProcessor(data, cardResolvedEventHandler);

		addHandler(CardPlayedEvent.class, new CardPlayedEventValidator(), new DoNothingConsumer<>(), true);
		addHandler(CardPlayedEvent.class, cpeHandler);
		addHandler(CardPlayedEvent.class, new CardPlayedEventVisualSyncHandler(visualSync));
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
		queueProcessor.processAll();
		dispatcher.dispatch(networkSync);
		GameState state = data.state();

		for (int y = -RENDER_RADIUS; y <= RENDER_RADIUS; y++) {
			for (int x = -RENDER_RADIUS; x <= RENDER_RADIUS; x++) {
				Vector2i chunkPos = camera.chunkPos().add(x, y);
				if (state.worldMap().chunk(chunkPos) == null) {
					state.worldMap().addChunk(state.worldMap().generateChunk(chunkPos));
					List<CardPlayer> generateActors = state.worldMap().generateActors(chunkPos, state);
					for (CardPlayer actor : generateActors) {
						actor.displayer().init(context().resourcePack());
						state.add(actor);
					}
				}
			}
		}

		state.chainHeap().processAll(data, visualSync);
		state.actors().forEach(a -> a.update(state));
		pushAll(visualSync);

	}

	public GameCamera camera() {
		return camera;
	}

}
