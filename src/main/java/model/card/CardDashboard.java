package model.card;

import model.GameState;
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

	public CardDashboard copy(GameState state) {
		CardDashboard copy = new CardDashboard();
		copy.hand = this.hand.shallowCopy(state);
		copy.deck = this.deck.shallowCopy(state);
		copy.discard = this.discard.shallowCopy(state);
		copy.queue = this.queue.deepCopy(state);
		return copy;
	}

}
