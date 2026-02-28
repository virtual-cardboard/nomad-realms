package nomadrealms.context.game.zone;

import nomadrealms.context.game.card.Card;
import nomadrealms.context.game.event.CardPlayedEvent;

public class CardStackEntry implements Card {

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

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void incrementCounter() {
		counter++;
	}

	public boolean isReady() {
		return counter >= event.card().card().resolutionTime();
	}

	public float getProgress() {
		return (float) counter / event.card().card().resolutionTime();
	}

}
