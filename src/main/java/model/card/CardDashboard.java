package model.card;

import model.task.Task;

public class CardDashboard {

	private CardZone hand = new CardZone(8);
	private CardZone deck = new CardZone(120);
	private CardZone discard = new CardZone();
	private CardQueue queue = new CardQueue(5);
	private Task task;

	public CardZone hand() {
		return hand;
	}

	public CardZone deck() {
		return deck;
	}

	public CardZone discard() {
		return discard;
	}

	public CardQueue queue() {
		return queue;
	}

	public Task task() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * Copies everything but the task to a new CardDashboard.
	 * 
	 * @return a copy of this card dashboard
	 */
	public CardDashboard copy() {
		CardDashboard copy = new CardDashboard();
		copy.hand = this.hand.copy();
		copy.deck = this.deck.copy();
		copy.discard = this.discard.copy();
		copy.queue = this.queue.copy();
		return copy;
	}

	public void cancelTask() {
		if (task != null) {
			task.cancel();
		}
	}

}
