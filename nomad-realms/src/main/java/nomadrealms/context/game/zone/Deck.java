package nomadrealms.context.game.zone;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.card.WorldCard;

public class Deck extends WorldCardZone {

	private final WorldCardZone discardZone = new WorldCardZone();

	public Deck() {
		super();
	}

	public void shuffle() {
		List<WorldCard> newCards = new ArrayList<>();
		while (!cards.isEmpty()) {
			int index = (int) (Math.random() * cards.size());
			newCards.add(cards.remove(index));
		}
		cards = newCards;
	}

	public void restock() {
		List<WorldCard> discardCards = discardZone.getCards();
		discardZone.clear();
		addCards(discardCards);
		shuffle();
	}

	public WorldCard draw() {
		if (cards.isEmpty()) {
			return null;
		}
		return cards.remove(0);
	}

	public WorldCard peek() {
		if (cards.isEmpty()) {
			return null;
		}
		return cards.get(0);
	}

	public WorldCardZone discardZone() {
		return discardZone;
	}

	@Override
	public void reindex() {
		super.reindex();
		discardZone.reindex();
	}

	@Override
	public String toString() {
		return "Deck{" +
				"cards=" + cards +
				", discardZone=" + discardZone +
				'}';
	}
}
