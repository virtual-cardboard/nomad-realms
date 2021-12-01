package context.game.visuals.handler;

import static model.card.CardType.CANTRIP;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.visualssync.CardResolvedSyncEvent;

public class CardResolvedSyncEventHandler implements Consumer<CardResolvedSyncEvent> {

	private NomadsGameData data;
	private CardDashboardGui dashboardGui;
	private RootGui rootGui;

	public CardResolvedSyncEventHandler(NomadsGameData data, CardDashboardGui dashboardGui, RootGui rootGui) {
		this.data = data;
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
	}

	/**
	 * If the card resolved is a cantrip, then it would have been put in the correct
	 * place by {@link CardPlayedSyncEventHandler}.
	 */
	@Override
	public void accept(CardResolvedSyncEvent t) {
		if (t.player() != data.player() || t.card().type() == CANTRIP) {
			return;
		}
		CardGui cardGui = dashboardGui.getCardGui(t.card());
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		dashboardGui.queue().removeCardGui(cardGui);
		dashboardGui.discard().addCardGui(cardGui);
		dashboardGui.discard().resetTargetPositions(rootGui.dimensions());
	}

}
