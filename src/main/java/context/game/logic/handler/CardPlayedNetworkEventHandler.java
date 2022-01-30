package context.game.logic.handler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.network.game.CardPlayedNetworkEvent;
import model.card.WorldCard;
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
		CardPlayedEvent cpe = new CardPlayedEvent(t.player(), t.card(), t.target());
		System.out.println("Network event: " + card + ", played by " + t.player());
		addToQueueHandler.accept(cpe);
	}

}
