package context.game.logic.handler;

import static model.card.expression.CardTargetType.typify;

import java.util.Queue;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.logicprocessing.CardPlayedEvent;
import event.network.p2p.game.CardPlayedNetworkEvent;
import model.actor.CardPlayer;
import model.card.WorldCard;
import model.id.Id;
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
		WorldCard card = currentState.card(t.cardId());
		CardPlayer cardPlayer = currentState.cardPlayer(t.playerId());
		Id targetId;
		if (card.effect().targetType == null) {
			targetId = null;
		} else {
			targetId = typify(t.targetId(), card.effect().targetType).getFrom(currentState).id();
		}
		CardPlayedEvent cpe = new CardPlayedEvent(cardPlayer.id(), targetId, card.id());
		System.out.println("Network event: " + card + ", played by " + t.playerId());
		cardPlayedEventQueue.add(cpe);
	}

}
