package context.game.logic;

import context.game.logic.handler.CardResolvedEventHandler;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.GameCard;
import model.state.GameState;

/**
 * Processes card queues of {@link CardPlayer}s.
 * 
 * @author Jay
 *
 */
public class QueueProcessor {

	private CardResolvedEventHandler cardResolvedEventHandler;

	public QueueProcessor(CardResolvedEventHandler cardResolvedEventHandler) {
		this.cardResolvedEventHandler = cardResolvedEventHandler;
	}

	public void processAll(GameState state) {
		for (CardPlayer cardPlayer : state.cardPlayers()) {
			CardQueue queue = cardPlayer.cardDashboard().queue();
			if (!queue.empty() && !queue.locked()) {
				if (queue.tickCount() == queue.first().card().cost() * 10) {
					queue.resetTicks();
					CardPlayedEvent cpe = queue.poll();
					GameCard card = cpe.card();
					if (card.effect().condition.test(cpe.player(), cpe.target())) {
						CardResolvedEvent cre = new CardResolvedEvent(cpe.player(), card, cpe.target());
						cardResolvedEventHandler.accept(cre);
						queue.setLocked(true);
					} else {
						CardDashboard dashboard = cardPlayer.cardDashboard();
						if (!dashboard.hand().full()) {
							dashboard.hand().add(card);
						} else {
							dashboard.discard().add(card);
						}
					}
				} else {
					queue.increaseTick();
				}
			}
		}
	}

}
