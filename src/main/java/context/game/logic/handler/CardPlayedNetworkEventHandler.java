package context.game.logic.handler;

import static model.card.expression.CardTargetType.typify;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.network.game.CardPlayedNetworkEvent;
import model.actor.CardPlayer;
import model.card.WorldCard;
import model.id.ID;
import model.state.GameState;

public class CardPlayedNetworkEventHandler implements Consumer<CardPlayedNetworkEvent> {

	private CardPlayedEventAddToQueueHandler addToQueueHandler;
	private NomadsGameData data;

	public CardPlayedNetworkEventHandler(NomadsGameData data, CardPlayedEventAddToQueueHandler addToQueueHandler) {
		this.data = data;
		this.addToQueueHandler = addToQueueHandler;
	}

	@Override
	public void accept(CardPlayedNetworkEvent t) {
		GameState state = data.nextState();
		WorldCard card = state.card(t.card());
		CardPlayer cardPlayer = state.cardPlayer(t.player());
		ID<?> targetID;
		if (card.effect().targetType == null) {
			targetID = null;
		} else {
			targetID = typify(t.target(), card.effect().targetType);
		}
		CardPlayedEvent cpe = new CardPlayedEvent(cardPlayer.id(), targetID, card.id());
		System.out.println("Network event: " + card + ", played by " + t.player());
		addToQueueHandler.accept(cpe);
	}

}
