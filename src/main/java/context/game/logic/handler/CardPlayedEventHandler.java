
package context.game.logic.handler;

import static model.card.CardType.CANTRIP;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.GameCard;

public class CardPlayedEventHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private CardResolvedEventHandler creHandler;

	public CardPlayedEventHandler(NomadsGameData data, CardResolvedEventHandler creHandler) {
		this.data = data;
		this.creHandler = creHandler;
	}

	@Override
	public void accept(CardPlayedEvent event) {
		CardDashboard dashboard = data.state().dashboard(event.player());
		GameCard card = event.card();
		int index = dashboard.hand().indexOf(card.id());
		dashboard.hand().remove(index);
		if (card.type() == CANTRIP) {
			CardResolvedEvent cre = new CardResolvedEvent(event.player(), card, event.target());
			creHandler.accept(cre);
		} else {
			CardQueue queue = dashboard.queue();
			queue.append(event);
		}
	}

}
