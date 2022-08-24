package context.game.visuals.handler;

import static model.card.CardType.CANTRIP;
import static model.card.CardType.TASK;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.game.visuals.gui.dashboard.WorldCardGui;
import context.visuals.gui.RootGui;
import event.logicprocessing.CardPlayedEvent;
import model.card.WorldCard;

public class CardPlayedEventVisualHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private NomadsGameVisuals visuals;
	private RootGui rootGui;

	public CardPlayedEventVisualHandler(NomadsGameData data, NomadsGameVisuals visuals, RootGui rootGui) {
		this.data = data;
		this.visuals = visuals;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardPlayedEvent t) {
		CardDashboardGui dashboardGui = visuals.dashboardGui();
		if (!t.playerID().equals(data.playerID())) {
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
		WorldCard card = t.cardID().getFrom(data.currentState());
		if (card.type() == CANTRIP || card.type() == TASK) {
			dashboardGui.discard().addChild(cardGui);
		} else {
			dashboardGui.queue().addChild(cardGui);
		}
		dashboardGui.resetTargetPositions(rootGui.dimensions(), data.settings());
	}

}
