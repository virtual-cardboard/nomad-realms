package nomadrealms.card.zone;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.card.card.GameCard;
import nomadrealms.card.card.WorldCard;

public class Deck extends CardZone<WorldCard> {

	public Deck() {
		super();
	}

	public void add(WorldCard card) {
		cards.add(card);
	}

	public void shuffle() {
		List<WorldCard> newCards = new ArrayList<>();
		while (!cards.isEmpty()) {
			int index = (int) (Math.random() * cards.size());
			newCards.add(cards.remove(index));
		}
		cards = newCards;
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

}
