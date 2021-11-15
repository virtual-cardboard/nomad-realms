
package context.game.logic;

import java.util.Queue;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import event.game.CardPlayedEvent;
import event.game.expression.ChainEvent;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.CardType;
import model.card.GameCard;
import model.card.effect.CardEffect;

public class CardPlayedEventHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private NomadsGameVisuals visuals;
	private Queue<ChainEvent> expressionQueue;

	public CardPlayedEventHandler(NomadsGameData data, NomadsGameVisuals visuals, Queue<ChainEvent> expressionQueue) {
		this.data = data;
		this.visuals = visuals;
		this.expressionQueue = expressionQueue;
	}

	@Override
	public void accept(CardPlayedEvent event) {
		CardDashboard dashboard = data.state().dashboard(data.player());
		GameCard card = event.card();
		int index = dashboard.hand().indexOf(card.id());
		dashboard.hand().delete(index);

		CardDashboardGui dashboardGui = visuals.dashboardGui();
		CardGui cardGui = dashboardGui.hand().removeCardGui(index);
		if (card.type() == CardType.CANTRIP) {
			dashboard.discard().addTop(card);
			playCard(event);
			dashboardGui.discard().addCardGui(cardGui);
		} else {
			CardQueue queue = dashboard.queue();
			queue.append(event);
//			data.state().dashboard(data.player()).setQueueResolutionTimeStart(tick);
			dashboardGui.queue().addCardGui(cardGui);
		}
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		dashboardGui.resetTargetPositions(visuals.rootGui().dimensions());
	}

	private void playCard(CardPlayedEvent cpe) {
		System.out.println("Played card " + cpe.card().name());
		CardEffect effect = cpe.card().effect();
		if (effect.expression != null) {
			effect.expression.process(cpe.player(), cpe.target(), data.state(), expressionQueue);
			System.out.println("Triggered effect!");
		} else {
			System.out.println("Null effect");
		}
	}

}
