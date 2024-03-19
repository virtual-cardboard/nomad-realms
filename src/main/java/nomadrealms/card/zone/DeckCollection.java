package nomadrealms.card.zone;

import static nomadrealms.card.card.GameCard.ATTACK;
import static nomadrealms.card.card.GameCard.HEAL;
import static nomadrealms.card.card.GameCard.MOVE;

public class DeckCollection {

	private Deck deck1, deck2, deck3, deck4;

	public DeckCollection() {
		initializeDecks();
	}

	private void initializeDecks() {
		this.deck1 = initializeDeck1();
		this.deck2 = initializeDeck1();
		this.deck3 = initializeDeck1();
		this.deck4 = initializeDeck1();
	}

	private Deck initializeDeck1() {
		Deck deck = new Deck();
		deck.add(MOVE);
		deck.add(HEAL);
		deck.add(ATTACK);
		deck.shuffle();
		return deck;
	}

	public Deck deck1() {
		return deck1;
	}

	public Deck deck2() {
		return deck2;
	}

	public Deck deck3() {
		return deck3;
	}

	public Deck deck4() {
		return deck4;
	}


}
