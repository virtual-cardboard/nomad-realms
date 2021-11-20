package context.game.visuals.handler;

import java.util.function.Consumer;

import context.ResourcePack;
import context.game.NomadsGameData;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.visualssync.CardDrawnSyncEvent;

public class CardDrawnSyncEventHandler implements Consumer<CardDrawnSyncEvent> {

	private NomadsGameData data;
	private CardDashboardGui dashboardGui;
	private ResourcePack resourcePack;
	private RootGui rootGui;

	public CardDrawnSyncEventHandler(NomadsGameData data, CardDashboardGui dashboardGui, ResourcePack resourcePack, RootGui rootGui) {
		this.data = data;
		this.dashboardGui = dashboardGui;
		this.resourcePack = resourcePack;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardDrawnSyncEvent t) {
		if (t.player() != data.player()) {
			return;
		}
		CardGui cardGui = dashboardGui.getCardGui(t.card());
		if (cardGui == null) {
			cardGui = new CardGui(t.card(), resourcePack);
			cardGui.setPos(dashboardGui.deck().topLeftPos(rootGui.dimensions()));
		} else {
			dashboardGui.hand().removeCardGui(cardGui);
		}
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		cardGui.unhover();
		dashboardGui.hand().addCardGui(cardGui);
		dashboardGui.resetTargetPositions(rootGui.dimensions());
	}

}
