package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.CardResolvedEvent;

public class CardResolvedEventVisualHandler implements Consumer<CardResolvedEvent> {

	private CardDashboardGui dashboardGui;
	private RootGui rootGui;

	public CardResolvedEventVisualHandler(CardDashboardGui dashboardGui, RootGui rootGui) {
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardResolvedEvent t) {
		CardGui cardGui = dashboardGui.getCardGui(t.card());
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		dashboardGui.queue().removeCardGui(cardGui);
		dashboardGui.discard().addCardGui(cardGui);
		dashboardGui.discard().resetTargetPositions(rootGui.dimensions());
	}

}
