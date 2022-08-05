package context.game.visuals.handler;

import static model.card.CardType.CANTRIP;
import static model.card.CardType.TASK;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.game.visuals.gui.dashboard.WorldCardGui;
import context.visuals.gui.RootGui;
import event.logicprocessing.CardPlayedEvent;
import model.card.WorldCard;

public class CardPlayedEventVisualHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private CardDashboardGui dashboardGui;
	private RootGui rootGui;

	public CardPlayedEventVisualHandler(NomadsGameData data, CardDashboardGui dashboardGui, RootGui rootGui) {
		this.data = data;
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardPlayedEvent t) {
		if (t.playerID() != data.playerID()) {
			return;
		}
		WorldCardGui cardGui = dashboardGui.getCardGui(t.cardID());
		if (!dashboardGui.hand().contains(cardGui)) {
			return;
		}
		cardGui.setDragged(false);
		cardGui.setLockTargetPos(false);
		cardGui.unhover(data.settings());
		dashboardGui.hand().removeChild(cardGui);
		WorldCard card = t.cardID().getFrom(data.previousState());
		if (card.type() == CANTRIP || card.type() == TASK) {
			dashboardGui.discard().addChild(cardGui);
		} else {
			dashboardGui.queue().addChild(cardGui);
		}
		dashboardGui.resetTargetPositions(rootGui.dimensions(), data.settings());
	}

}
