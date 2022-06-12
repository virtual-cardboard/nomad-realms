package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import event.logicprocessing.CardPlayedEvent;

public class CardPlayedEventAddToQueueHandler implements Consumer<CardPlayedEvent> {

	private Queue<CardPlayedEvent> queue;

	public CardPlayedEventAddToQueueHandler(Queue<CardPlayedEvent> queue) {
		this.queue = queue;
	}

	@Override
	public void accept(CardPlayedEvent t) {
		queue.add(t);
	}

}
