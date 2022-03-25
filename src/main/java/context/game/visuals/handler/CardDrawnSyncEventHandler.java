package context.game.visuals.handler;

import java.util.function.Consumer;

import context.ResourcePack;
import context.game.NomadsGameData;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.game.visuals.gui.dashboard.WorldCardGui;
import context.visuals.gui.RootGui;
import event.game.sync.CardDrawnSyncEvent;

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
		if (t.playerID() != data.playerID()) {
			return;
		}
		WorldCardGui cardGui = dashboardGui.getCardGui(t.cardID());
		if (cardGui == null) {
			cardGui = new WorldCardGui(t.cardID().getFrom(data.previousState()), resourcePack);
			cardGui.setCenterPos(dashboardGui.deck().centerPos(rootGui.dimensions()));
		} else {
			dashboardGui.hand().removeChild(cardGui);
		}
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		cardGui.unhover(data.settings());
		dashboardGui.hand().addChild(cardGui);
		dashboardGui.resetTargetPositions(rootGui.dimensions(), data.settings());
	}

}
