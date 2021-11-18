package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.CardPlayedEvent;
import model.card.CardType;

public class CardPlayedEventVisualHandler implements Consumer<CardPlayedEvent> {

	private CardDashboardGui dashboardGui;
	private RootGui rootGui;

	public CardPlayedEventVisualHandler(CardDashboardGui dashboardGui, RootGui rootGui) {
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardPlayedEvent t) {
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
