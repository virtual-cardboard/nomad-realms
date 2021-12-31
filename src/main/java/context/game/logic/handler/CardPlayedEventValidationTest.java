package context.game.logic.handler;

import java.util.function.Predicate;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import model.card.CardDashboard;
import model.state.GameState;

/**
 * A predicate that makes sure a card is never played out of the hand twice.
 * 
 * @author Jay
 */
public class CardPlayedEventValidationTest implements Predicate<CardPlayedEvent> {

	private NomadsGameData data;

	public CardPlayedEventValidationTest(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public boolean test(CardPlayedEvent event) {
		GameState state = data.nextState();
		CardDashboard dashboard = state.cardPlayer(event.playerID()).cardDashboard();
		int index = dashboard.hand().indexOf(event.cardID());
		return index == -1;
	}

}
