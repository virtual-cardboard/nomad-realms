package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.visuals.gui.CardGui;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.visuals.gui.RootGui;
import event.game.sync.CardShuffledSyncEvent;
import model.card.WorldCard;

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
		if (t.playerID() != data.playerID()) {
			return;
		}
		CardGui cardGui = dashboardGui.getCardGui(t.cardID());

		if (cardGui == null) {
			WorldCard card = t.cardID().getFrom(data.currentState());
			cardGui = new CardGui(card, data.context().resourcePack());
			cardGui.setCenterPos(dashboardGui.discard().centerPos(rootGui.dimensions()));
		} else {
			dashboardGui.discard().removeCardGui(cardGui);
		}
		System.out.println(cardGui.cardID().getFrom(data.currentState()).name() + " " + cardGui.cardID().toLongID() + " was shuffled");
		dashboardGui.deck().addCardGui(cardGui);
		cardGui.setTargetPos(dashboardGui.deck().centerPos(rootGui.dimensions()));
	}

}
