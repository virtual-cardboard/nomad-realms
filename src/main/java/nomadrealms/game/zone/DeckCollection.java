package nomadrealms.game.zone;

import static nomadrealms.game.card.GameCard.ATTACK;
import static nomadrealms.game.card.GameCard.HEAL;
import static nomadrealms.game.card.GameCard.MOVE;

import nomadrealms.game.card.WorldCard;

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
		deck.addCard(new WorldCard(MOVE));
		deck.addCard(new WorldCard(HEAL));
		deck.addCard(new WorldCard(ATTACK));
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

	public Deck[] decks() {
		return new Deck[] { deck1, deck2, deck3, deck4 };
	}

}
