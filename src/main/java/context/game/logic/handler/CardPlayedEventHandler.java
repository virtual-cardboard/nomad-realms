package context.game.logic.handler;

import static model.card.CardType.CANTRIP;
import static model.card.CardType.TASK;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.state.GameState;

public class CardPlayedEventHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private CardResolvedEventHandler creHandler;

	public CardPlayedEventHandler(NomadsGameData data, CardResolvedEventHandler creHandler) {
		this.data = data;
		this.creHandler = creHandler;
	}

	/**
	 * If the card is a cantrip, then it resolves immediately and is handled by
	 * {@link CardResolvedEventHandler}.
	 */
	@Override
	public void accept(CardPlayedEvent event) {
		GameState state = data.nextState();
		CardPlayer player = state.cardPlayer(event.playerID());
		WorldCard card = state.card(event.cardID());
		CardDashboard dashboard = player.cardDashboard();
		int index = dashboard.hand().indexOf(card.id());
		dashboard.hand().remove(index);
		if (card.type() == CANTRIP) {
			creHandler.accept(new CardResolvedEvent(event.playerID(), event.cardID(), event.targetID()));
		} else if (card.type() == TASK) {
			if (dashboard.task() != null) {
				dashboard.task().cancel();
			}
			creHandler.accept(new CardResolvedEvent(event.playerID(), event.cardID(), event.targetID()));
		} else {
			dashboard.queue().append(event);
		}
	}

}
