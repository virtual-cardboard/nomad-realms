package nomadrealms.context.game.zone;

import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.render.ui.custom.card.StackIcon;

public class CardStackEntry extends CardPlayedEvent {

	private transient StackIcon icon;
	private int counter;

	public CardStackEntry(CardPlayedEvent event) {
		super(event.originalCard(), event.source(), event.target());
		this.counter = 0;
	}

	/**
	 * @return the number of ticks this card has been on the stack.
	 */
	public int counter() {
		return counter;
	}

	public void incrementCounter() {
		counter++;
	}

	public boolean isReady() {
		return counter >= card().resolutionTime();
	}

	public float getProgress() {
		return (float) counter / card().resolutionTime();
	}

	public StackIcon icon() {
		if (icon == null) {
			icon = new StackIcon(this);
		}
		return icon;
	}

}
