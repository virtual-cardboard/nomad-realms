package context.game.logic.handler;

import java.util.function.Predicate;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import model.card.CardDashboard;
import model.card.GameCard;

public class CardPlayedEventValidator implements Predicate<CardPlayedEvent> {

	private NomadsGameData data;

	public CardPlayedEventValidator(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public boolean test(CardPlayedEvent event) {
		CardDashboard dashboard = data.state().dashboard(event.player());
		GameCard card = event.card();
		int index = dashboard.hand().indexOf(card.id());
		return index == -1;
	}

}
