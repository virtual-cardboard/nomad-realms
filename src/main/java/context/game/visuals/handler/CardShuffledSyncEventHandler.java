package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.visualssync.CardShuffledSyncEvent;

public class CardShuffledSyncEventHandler implements Consumer<CardShuffledSyncEvent> {

	private NomadsGameData data;
	private CardDashboardGui dashboardGui;
	private RootGui rootGui;

	public CardShuffledSyncEventHandler(NomadsGameData data, CardDashboardGui dashboardGui, RootGui rootGui) {
		this.data = data;
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardShuffledSyncEvent t) {
		if (t.player() != data.player()) {
			return;
		}
		CardGui cardGui = dashboardGui.getCardGui(t.card());
		if (cardGui == null) {
			cardGui = new CardGui(t.card(), data.context().resourcePack());
			cardGui.setPos(dashboardGui.discard().centerPos(rootGui.dimensions()));
		} else {
			dashboardGui.discard().removeCardGui(cardGui);
		}
		dashboardGui.deck().addCardGui(cardGui);
		cardGui.setTargetPos(dashboardGui.deck().centerPos(rootGui.dimensions()));
	}

}
