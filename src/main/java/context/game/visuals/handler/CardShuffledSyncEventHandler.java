package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.game.visuals.gui.dashboard.WorldCardGui;
import context.visuals.gui.RootGui;
import event.sync.CardShuffledSyncEvent;
import model.card.WorldCard;

public class CardShuffledSyncEventHandler implements Consumer<CardShuffledSyncEvent> {

	private NomadsGameData data;
	private NomadsGameVisuals visuals;
	private RootGui rootGui;

	public CardShuffledSyncEventHandler(NomadsGameData data, NomadsGameVisuals visuals, RootGui rootGui) {
		this.data = data;
		this.visuals = visuals;
		this.rootGui = rootGui;
	}

	@Override
	public void accept(CardShuffledSyncEvent t) {
		CardDashboardGui dashboardGui = visuals.dashboardGui();
		if (!t.playerID().equals(data.playerID())) {
			return;
		}
		WorldCardGui cardGui = dashboardGui.getCardGui(t.cardID());

		if (cardGui == null) {
			WorldCard card = t.cardID().getFrom(data.nextState());
			cardGui = new WorldCardGui(card, data.resourcePack());
			cardGui.setCenterPos(dashboardGui.discard().centerPos(rootGui.dimensions()));
		} else {
			dashboardGui.discard().removeChild(cardGui);
		}
		dashboardGui.deck().addChild(cardGui);
		cardGui.setTargetPos(dashboardGui.deck().centerPos(rootGui.dimensions()));
	}

}
