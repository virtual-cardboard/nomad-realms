package model.card;

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

}
