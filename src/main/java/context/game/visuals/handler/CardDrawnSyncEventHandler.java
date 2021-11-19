package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.visualssync.CardDrawnSyncEvent;
import model.card.CardType;

public class CardDrawnSyncEventHandler implements Consumer<CardDrawnSyncEvent> {

	private CardDashboardGui dashboardGui;
	private RootGui rootGui;

	public CardDrawnSyncEventHandler(CardDashboardGui dashboardGui, RootGui rootGui) {
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardDrawnSyncEvent t) {
		CardGui cardGui = dashboardGui.getCardGui(t.card());
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		cardGui.unhover();
		dashboardGui.hand().removeCardGui(cardGui);
		if (t.card().type() == CardType.CANTRIP) {
			dashboardGui.discard().addCardGui(cardGui);
		} else {
			dashboardGui.queue().addCardGui(cardGui);
		}
		dashboardGui.resetTargetPositions(rootGui.dimensions());
	}

}
