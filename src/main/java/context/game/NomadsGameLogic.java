package context.game;

import java.util.ArrayDeque;
import java.util.Queue;

import common.event.GameEvent;
import context.connect.PeerConnectRequestEvent;
import context.game.logic.CardHoveredEventHandler;
import context.game.logic.CardPlayedEventHandler;
import context.game.logic.PeerConnectRequestEventHandler;
import context.game.visuals.gui.CardGui;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.game.CardHoveredEvent;
import event.game.CardPlayedEvent;
import event.game.expression.CardExpressionEvent;
import event.network.CardHoveredNetworkEvent;
import event.network.CardPlayedNetworkEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.RandomAccessArrayDeque;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;
	private NomadsGameVisuals visuals;

	private Queue<CardExpressionEvent> expressionQueue = new ArrayDeque<>();

	private int tick;
	private CardPlayedEventHandler cardPlayedHandler;

	public NomadsGameLogic(PacketAddress peerAddress) {
	}

	@Override
	protected void init() {
		data = (NomadsGameData) context().data();
		visuals = (NomadsGameVisuals) context().visuals();
		cardPlayedHandler = new CardPlayedEventHandler(data, visuals, expressionQueue);
		addHandler(CardPlayedEvent.class, cardPlayedHandler);
		addHandler(PeerConnectRequestEvent.class, new PeerConnectRequestEventHandler(context()));
		addHandler(CardHoveredEvent.class, new CardHoveredEventHandler(context()));
		addHandler(CardHoveredNetworkEvent.class, (event) -> System.out.println("Opponent hovered"));
	}

	@Override
	public void update() {
		CardDashboard dashboard = data.state().dashboard(data.player());
		RandomAccessArrayDeque<CardPlayedEvent> queue = dashboard.queue();
		while (!queue.isEmpty() && dashboard.timeUntilResolution(tick) <= 0) {
			CardPlayedEvent cardPlayedEvent = queue.poll();
			CardGui cardGui = visuals.dashboardGui().queue().removeCardGui(0);
			visuals.dashboardGui().discard().addCardGui(cardGui);
			dashboard.setQueueResolutionTimeStart(tick);
			visuals.dashboardGui().discard().resetTargetPositions(visuals.rootGui().dimensions());
		}
		handleAllEvents();
		tick++;
	}

	private void handleAllEvents() {
		while (!eventQueue().isEmpty()) {
			GameEvent event = eventQueue().poll();
			if (event instanceof CardPlayedNetworkEvent) {
				CardPlayedNetworkEvent cardPlayed = (CardPlayedNetworkEvent) event;
				CardPlayer actor = data.state().cardPlayer(cardPlayed.player());
				System.out.println("Received CardPlayedNetworkEvent");
				if (actor != null) {
					CardPlayer cardPlayer = actor;
					CardDashboard dashboard = data.state().dashboard(cardPlayer);
					int foundCard = -1;
					for (int i = 0, size = dashboard.hand().size(); i < size; i++) {
						if (dashboard.hand().card(i).id() == cardPlayed.card()) {
							foundCard = i;
						}
					}
					if (foundCard == -1) {
						System.out.println("Card with id is not in hand in CardPlayedNetworkEvent");
					}
//					GameCard card = dashboard.hand().drawCard(foundCard);
//					cardPlayed.target();
					System.out.println("Validated info in CardPlayedNetworkEvent");
				} else {
					System.out.println("Card playedBy is not a CardPlayer in CardPlayedNetworkEvent");
				}
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
			}
		}
	}

}
