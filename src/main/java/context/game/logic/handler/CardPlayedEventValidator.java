package context.game.logic.handler;

import java.util.function.Predicate;

import event.game.logicprocessing.CardPlayedEvent;
import model.card.CardDashboard;
import model.card.GameCard;

/**
 * A predicate that makes sure a card is never played out of the hand twice.
 * 
 * @author Jay
 */
public class CardPlayedEventValidator implements Predicate<CardPlayedEvent> {

	@Override
	public boolean test(CardPlayedEvent event) {
		CardDashboard dashboard = event.player().cardDashboard();
		GameCard card = event.card();
		int index = dashboard.hand().indexOf(card.id());
		return index == -1;
	}

}
