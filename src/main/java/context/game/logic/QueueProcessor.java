package context.game.logic;

import context.game.logic.handler.CardResolvedEventHandler;
import engine.common.ContextQueues;
import event.logicprocessing.CardPlayedEvent;
import event.logicprocessing.CardResolvedEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.WorldCard;
import model.id.Id;
import model.state.GameState;

/**
 * Processes card queues of {@link CardPlayer}s.
 *
 * @author Jay
 */
public class QueueProcessor {

	private CardResolvedEventHandler cardResolvedEventHandler;

	public QueueProcessor(CardResolvedEventHandler cardResolvedEventHandler) {
		this.cardResolvedEventHandler = cardResolvedEventHandler;
	}

	public void processAll(GameState state, ContextQueues contextQueues) {
		for (CardPlayer cardPlayer : state.cardPlayers()) {
			CardQueue queue = cardPlayer.cardDashboard().queue();
			if (!queue.empty() && !queue.locked()) {
				CardPlayedEvent first = queue.first();
				CardPlayer player = first.playerID().getFrom(state);
				WorldCard card = first.cardID().getFrom(state);
				if (queue.tickCount() == card.cost() * 10) {
					queue.resetTicks();
					queue.poll();
					Id targetId = first.targetID();
					if (card.effect().targetPredicate.test(player, targetId != null ? targetId.getFrom(state) : null)) {
						CardResolvedEvent cre = new CardResolvedEvent(first.playerID(), first.cardID(), first.targetID());
						cardResolvedEventHandler.accept(cre);
						player.cardDashboard().discard().addTop(card);
						contextQueues.pushEventFromLogic(cre);
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
