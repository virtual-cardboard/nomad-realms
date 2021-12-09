package context.game;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import common.event.GameEvent;
import context.game.logic.QueueProcessor;
import context.game.logic.handler.*;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.game.logicprocessing.CardPlayedEvent;
import event.network.CardPlayedNetworkEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import networking.GameNetwork;
import networking.NetworkEventDispatcher;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;
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

		queueProcessor = new QueueProcessor(data, cardResolvedEventHandler);

		addHandler(CardPlayedEvent.class, new CardPlayedEventValidator(), new DoNothingConsumer<>(), true);
		addHandler(CardPlayedEvent.class, cpeHandler);
		addHandler(CardPlayedEvent.class, new CardPlayedEventVisualSyncHandler(visualSync));
		addHandler(CardPlayedEvent.class, new CardPlayedEventNetworkSyncHandler(networkSync));

		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventHandler(data, cpeHandler));
		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventVisualSyncHandler(data, visualSync));

		addHandler(PeerConnectRequestEvent.class, new InGamePeerConnectRequestEventHandler(networkSync, visualSync, nonce, username));
//		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(sync)); 
//		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
	}

	@Override
	public void update() {
		queueProcessor.process();
		dispatcher.dispatch(networkSync);
		data.state().chainHeap().processAll(data, visualSync);
		pushAll(visualSync);
	}

}
