package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.visualssync.CardPlayedSyncEvent;
import model.card.CardType;

public class CardPlayedSyncEventHandler implements Consumer<CardPlayedSyncEvent> {

	private CardDashboardGui dashboardGui;
	private RootGui rootGui;

	public CardPlayedSyncEventHandler(CardDashboardGui dashboardGui, RootGui rootGui) {
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardPlayedSyncEvent t) {
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
