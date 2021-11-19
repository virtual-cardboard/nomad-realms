package context.game;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import common.event.GameEvent;
import context.connect.PeerConnectRequestEvent;
import context.game.logic.CardHoveredEventHandler;
import context.game.logic.CardPlayedEventHandler;
import context.game.logic.CardResolvedEventHandler;
import context.game.logic.PeerConnectRequestEventHandler;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import event.game.playerinput.PlayerHoveredCardEvent;
import event.network.CardHoveredNetworkEvent;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.GameCard;
import model.chain.ChainHeap;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;

	private Queue<GameEvent> sync = new ArrayBlockingQueue<>(5);
	private int tick;

	private CardResolvedEventHandler cardResolvedEventHandler;

	public NomadsGameLogic(PacketAddress peerAddress) {
	}

	@Override
	protected void init() {
		data = (NomadsGameData) context().data();
		addHandler(CardPlayedEvent.class, new CardPlayedEventHandler(data, sync));
		addHandler(PeerConnectRequestEvent.class, new PeerConnectRequestEventHandler(context()));
//		addHandler(DrawCardEvent.class, new DrawCardEventHandler(data, sync));
		addHandler(PlayerHoveredCardEvent.class, new CardHoveredEventHandler(context()));
		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
		addHandler(CardResolvedEvent.class, cardResolvedEventHandler = new CardResolvedEventHandler(data, sync));
	}

	@Override
	public void update() {
		CardDashboard dashboard = data.state().dashboard(data.player());
		CardQueue queue = dashboard.queue();
		if (!queue.empty()) {
			if (queue.tickCount() == queue.first().card().cost() * 10) {
				queue.resetTicks();
				CardPlayedEvent cpe = queue.poll();
				GameCard card = cpe.card();
//				event.process(data.state(), chain);
//				dashboard.discard().addTop(card);
				CardResolvedEvent cre = new CardResolvedEvent(cpe.player(), card, cpe.target());
				cardResolvedEventHandler.accept(cre);
			} else {
				queue.increaseTick();
			}
		}
		data.state().chainHeap().processAll(data, sync);
		pushAndClearBuffer(sync);
		handleAllEvents();
		tick++;
	}

	private void handleAllEvents() {
//		while (!eventQueue().isEmpty()) {
//			GameEvent event = eventQueue().poll();
//			if (event instanceof CardPlayedNetworkEvent) {
//				CardPlayedNetworkEvent cardPlayed = (CardPlayedNetworkEvent) event;
//				CardPlayer actor = data.state().cardPlayer(cardPlayed.player());
//				System.out.println("Received CardPlayedNetworkEvent");
//				if (actor != null) {
//					CardPlayer cardPlayer = actor;
//					CardDashboard dashboard = data.state().dashboard(cardPlayer);
//					int foundCard = -1;
//					for (int i = 0, size = dashboard.hand().size(); i < size; i++) {
//						if (dashboard.hand().card(i).id() == cardPlayed.card()) {
//							foundCard = i;
//						}
//					}
//					if (foundCard == -1) {
//						System.out.println("Card with id is not in hand in CardPlayedNetworkEvent");
//					}
////					GameCard card = dashboard.hand().drawCard(foundCard);
////					cardPlayed.target();
//					System.out.println("Validated info in CardPlayedNetworkEvent");
//				} else {
//					System.out.println("Card playedBy is not a CardPlayer in CardPlayedNetworkEvent");
//				}
//			} else if (event instanceof DrawCardEvent) {
//				DrawCardEvent drawCardEvent = (DrawCardEvent) event;
//				CardDashboard dashboard = data.state().dashboard(data.player());
//				CardDashboardGui dashboardGui = visuals.dashboardGui();
//				for (int i = 0; i < drawCardEvent.num(); i++) {
//					if (dashboard.deck().size() == 0) {
//						break;
//					}
//					GameCard draw = dashboard.deck().drawTop();
//					dashboard.hand().addBottom(draw);
//					// TODO: move to visuals
//					CardGui cardGui;
//					if (dashboardGui.deck().numCardGuis() == 0) {
//						cardGui = new CardGui(draw, context().resourcePack());
//						cardGui.setPos(dashboardGui.deck().centerPos(visuals.rootGui().dimensions()));
//					} else {
//						cardGui = dashboardGui.deck().removeCardGui(0);
//					}
//					dashboardGui.hand().addCardGui(cardGui);
//				}
//				dashboardGui.resetTargetPositions(visuals.rootGui().dimensions());
//			}
//		}
	}

}
