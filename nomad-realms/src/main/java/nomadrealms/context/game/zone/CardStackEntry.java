package nomadrealms.context.game.zone;

import nomadrealms.context.game.card.Card;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.render.ui.custom.card.StackIcon;

public class CardStackEntry implements Card {

	private final CardPlayedEvent event;
	private transient StackIcon icon;
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

	public boolean isReady() {
		return counter >= event.card().card().resolutionTime();
	}

	public float getProgress() {
		return (float) counter / event.card().card().resolutionTime();
	}

	public StackIcon icon() {
		if (icon == null) {
			icon = new StackIcon(this);
		}
		return icon;
	}

	public void icon(StackIcon icon) {
		this.icon = icon;
	}

}
