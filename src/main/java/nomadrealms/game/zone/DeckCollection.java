package nomadrealms.game.zone;

public class DeckCollection {

	private Deck[] decks = new Deck[4];

	public DeckCollection() {
		initializeDecks();
	}

	private void initializeDecks() {
		this.decks[0] = new Deck();
		this.decks[1] = new Deck();
		this.decks[2] = new Deck();
		this.decks[3] = new Deck();
	}

	public Deck deck1() {
		return decks[0];
	}

	public Deck deck2() {
		return decks[1];
	}

	public Deck deck3() {
		return decks[2];
	}

	public Deck deck4() {
		return decks[3];
	}

	public Deck[] decks() {
		return decks;
	}

}
