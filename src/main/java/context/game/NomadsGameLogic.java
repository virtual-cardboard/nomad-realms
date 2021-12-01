package context.game;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import common.event.GameEvent;
import context.game.logic.handler.*;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import event.network.CardPlayedNetworkEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.GameCard;
import networking.GameNetwork;
import networking.NetworkEventDispatcher;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;
	private Queue<GameEvent> networkSync = new ArrayBlockingQueue<>(100);
	private Queue<GameEvent> visualSync = new ArrayBlockingQueue<>(100);
	private CardResolvedEventHandler cardResolvedEventHandler;

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

		CardPlayedEventHandler cpeHandler = new CardPlayedEventHandler(data);
		addHandler(CardPlayedEvent.class, cpeHandler);
		addHandler(CardPlayedEvent.class, new CardPlayedEventVisualSyncHandler(visualSync));
		addHandler(CardPlayedEvent.class, new CardPlayedEventNetworkSyncHandler(networkSync));

		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventHandler(data, cpeHandler));
		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventVisualSyncHandler(data, visualSync));

		addHandler(PeerConnectRequestEvent.class, new InGamePeerConnectRequestEventHandler(networkSync, visualSync, nonce, username));
		addHandler(CardResolvedEvent.class, cardResolvedEventHandler = new CardResolvedEventHandler(data, networkSync, visualSync));
//		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(sync)); 
//		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
	}

	@Override
	public void update() {
		for (CardDashboard dashboard : data.state().dashboards()) {
			CardQueue queue = dashboard.queue();
			if (!queue.empty()) {
				if (queue.tickCount() == queue.first().card().cost() * 10) {
					queue.resetTicks();
					CardPlayedEvent cpe = queue.poll();
					GameCard card = cpe.card();
					CardResolvedEvent cre = new CardResolvedEvent(cpe.player(), card, cpe.target());
					cardResolvedEventHandler.accept(cre);
				} else {
					queue.increaseTick();
				}
			}
		}
		dispatcher.dispatch(networkSync);
		data.state().chainHeap().processAll(data, visualSync);
		pushAll(visualSync);
	}

}
