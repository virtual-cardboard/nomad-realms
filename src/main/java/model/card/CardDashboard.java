package model.card;

import event.game.CardPlayedEvent;

public class CardDashboard {

	private CardZone hand = new CardZone(8);
	private CardZone deck = new CardZone(120);
	private CardZone discard = new CardZone();
	private RandomAccessArrayDeque<CardPlayedEvent> queue = new RandomAccessArrayDeque<>();

	public CardZone hand() {
		return hand;
	}

	public CardZone deck() {
		return deck;
	}

	public CardZone discard() {
		return discard;
	}

	public RandomAccessArrayDeque<CardPlayedEvent> queue() {
		return queue;
	}

}
