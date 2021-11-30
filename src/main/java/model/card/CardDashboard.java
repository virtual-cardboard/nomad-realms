package model.card;

import model.GameState;

public class CardDashboard {

	private CardZone hand = new CardZone(8);
	private CardZone deck = new CardZone(120);
	private CardZone discard = new CardZone();
	private CardQueue queue = new CardQueue(5);

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

	public CardDashboard copy(GameState state) {
		CardDashboard copy = new CardDashboard();
		copy.hand = this.hand.shallowCopy(state);
		copy.deck = this.deck.shallowCopy(state);
		copy.discard = this.discard.shallowCopy(state);
		copy.queue = this.queue.deepCopy(state);
		return copy;
	}

}
