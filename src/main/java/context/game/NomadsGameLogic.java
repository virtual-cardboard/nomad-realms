package context.game;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import common.event.GameEvent;
import context.connect.PeerConnectRequestEvent;
import context.game.logic.CardHoveredEventHandler;
import context.game.logic.CardPlayedEventHandler;
import context.game.logic.CardPlayedNetworkEventHandler;
import context.game.logic.CardResolvedEventHandler;
import context.game.logic.PeerConnectRequestEventHandler;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import event.game.playerinput.PlayerHoveredCardEvent;
import event.network.CardPlayedNetworkEvent;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.GameCard;
import networking.GameNetwork;
import networking.NetworkEventDispatcher;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;
	private Queue<GameEvent> sync = new ArrayBlockingQueue<>(5);
	private CardResolvedEventHandler cardResolvedEventHandler;

	private GameNetwork network = new GameNetwork();
	private NetworkEventDispatcher dispatcher;

	public NomadsGameLogic(PacketAddress peerAddress) {
	}

	@Override
	protected void init() {
		data = (NomadsGameData) context().data();
		dispatcher = new NetworkEventDispatcher(network, context().networkSend());
		CardPlayedEventHandler cpeHandler = new CardPlayedEventHandler(data, sync);
		addHandler(CardPlayedEvent.class, cpeHandler);
		addHandler(PeerConnectRequestEvent.class, new PeerConnectRequestEventHandler(sync));
		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(sync));
		addHandler(CardPlayedNetworkEvent.class, new CardPlayedNetworkEventHandler(data.state(), cpeHandler));
//		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
		addHandler(CardResolvedEvent.class, cardResolvedEventHandler = new CardResolvedEventHandler(data, sync));
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
		dispatcher.dispatch(sync);
		data.state().chainHeap().processAll(data, sync);
		pushAndClearBuffer(sync);
	}

}
