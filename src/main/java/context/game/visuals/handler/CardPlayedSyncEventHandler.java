package context.game.visuals.handler;

import static model.card.CardType.CANTRIP;
import static model.card.CardType.TASK;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.visualssync.CardPlayedSyncEvent;
import model.card.WorldCard;

public class CardPlayedSyncEventHandler implements Consumer<CardPlayedSyncEvent> {

	private NomadsGameData data;
	private CardDashboardGui dashboardGui;
	private RootGui rootGui;

	public CardPlayedSyncEventHandler(NomadsGameData data, CardDashboardGui dashboardGui, RootGui rootGui) {
		this.data = data;
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardPlayedSyncEvent t) {
		if (t.playerID() != data.playerID()) {
			return;
		}
		CardGui cardGui = dashboardGui.getCardGui(t.cardID());
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		cardGui.unhover(data.settings());
		dashboardGui.hand().removeCardGui(cardGui);
		WorldCard card = data.states().peekLast().card(t.cardID());
		if (card.type() == CANTRIP || card.type() == TASK) {
			dashboardGui.discard().addCardGui(cardGui);
		} else {
			dashboardGui.queue().addCardGui(0, cardGui);
		}
		dashboardGui.resetTargetPositions(rootGui.dimensions(), data.settings());
	}

}
