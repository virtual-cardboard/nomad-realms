package nomadrealms.context.game.zone;

import nomadrealms.context.game.event.CardPlayedEvent;

public class CardStackEntry {

	private final CardPlayedEvent event;
	private int counter;

	public CardStackEntry(CardPlayedEvent event) {
		this.event = event;
		this.counter = 0;
	}

	public CardPlayedEvent event() {
		return event;
	}

	public int counter() {
		return counter;
	}

	public void incrementCounter() {
		counter++;
	}

}
