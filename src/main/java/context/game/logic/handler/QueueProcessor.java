package context.game.logic.handler;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.GameCard;

public class QueueProcessor {

	private NomadsGameData data;
	private CardResolvedEventHandler cardResolvedEventHandler;

	public QueueProcessor(NomadsGameData data, CardResolvedEventHandler cardResolvedEventHandler) {
		this.data = data;
		this.cardResolvedEventHandler = cardResolvedEventHandler;
	}

	public void process() {
		for (CardDashboard dashboard : data.state().dashboards()) {
			CardQueue queue = dashboard.queue();
			if (!queue.empty()) {
				if (queue.tickCount() == queue.first().card().cost() * 10) {
					queue.resetTicks();
					CardPlayedEvent cpe = queue.poll();
					GameCard card = cpe.card();
					CardResolvedEvent cre = new CardResolvedEvent(cpe.player(), card, cpe.target());
					cardResolvedEventHandler.accept(cre);
				} else {
					queue.increaseTick();
				}
			}
		}
	}

}
