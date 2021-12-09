package context.game.logic;

import context.game.NomadsGameData;
import context.game.logic.handler.CardResolvedEventHandler;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import model.actor.CardPlayer;
import model.card.CardQueue;
import model.card.GameCard;

/**
 * Processes card queues of {@link CardPlayer}s.
 * 
 * @author Jay
 *
 */
public class QueueProcessor {

	private NomadsGameData data;
	private CardResolvedEventHandler cardResolvedEventHandler;

	public QueueProcessor(NomadsGameData data, CardResolvedEventHandler cardResolvedEventHandler) {
		this.data = data;
		this.cardResolvedEventHandler = cardResolvedEventHandler;
	}

	public void process() {
		for (CardPlayer cardPlayer : data.state().cardPlayers()) {
			CardQueue queue = cardPlayer.cardDashboard().queue();
			if (!queue.empty() && !queue.locked()) {
				if (queue.tickCount() == queue.first().card().cost() * 10) {
					queue.resetTicks();
					CardPlayedEvent cpe = queue.poll();
					GameCard card = cpe.card();
					CardResolvedEvent cre = new CardResolvedEvent(cpe.player(), card, cpe.target());
					cardResolvedEventHandler.accept(cre);
					queue.setLocked(true);
				} else {
					queue.increaseTick();
				}
			}
		}
	}

}
