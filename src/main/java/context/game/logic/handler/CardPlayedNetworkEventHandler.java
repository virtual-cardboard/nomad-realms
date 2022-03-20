package context.game.logic.handler;

import static model.card.expression.CardTargetType.typify;

import java.util.Queue;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.network.game.CardPlayedNetworkEvent;
import model.actor.CardPlayer;
import model.card.WorldCard;
import model.id.ID;
import model.state.GameState;

public class CardPlayedNetworkEventHandler implements Consumer<CardPlayedNetworkEvent> {

	private NomadsGameData data;
	private Queue<CardPlayedEvent> cardPlayedEventQueue;

	public CardPlayedNetworkEventHandler(NomadsGameData data, Queue<CardPlayedEvent> cardPlayedEventQueue) {
		this.data = data;
		this.cardPlayedEventQueue = cardPlayedEventQueue;
	}

	@Override
	public void accept(CardPlayedNetworkEvent t) {
		GameState currentState = data.currentState();
		WorldCard card = currentState.card(t.card());
		CardPlayer cardPlayer = currentState.cardPlayer(t.player());
		ID targetID;
		if (card.effect().targetType == null) {
			targetID = null;
		} else {
			targetID = typify(t.target(), card.effect().targetType).getFrom(currentState).id();
		}
		CardPlayedEvent cpe = new CardPlayedEvent(cardPlayer.id(), targetID, card.id());
		System.out.println("Network event: " + card + ", played by " + t.player());
		cardPlayedEventQueue.add(cpe);
	}

}
