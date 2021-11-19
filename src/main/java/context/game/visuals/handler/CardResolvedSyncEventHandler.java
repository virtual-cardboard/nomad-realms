package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.visualssync.CardResolvedSyncEvent;

public class CardResolvedSyncEventHandler implements Consumer<CardResolvedSyncEvent> {

	private CardDashboardGui dashboardGui;
	private RootGui rootGui;

	public CardResolvedSyncEventHandler(CardDashboardGui dashboardGui, RootGui rootGui) {
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardResolvedSyncEvent t) {
		CardGui cardGui = dashboardGui.getCardGui(t.card());
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		dashboardGui.queue().removeCardGui(cardGui);
		dashboardGui.discard().addCardGui(cardGui);
		dashboardGui.discard().resetTargetPositions(rootGui.dimensions());
	}

}
