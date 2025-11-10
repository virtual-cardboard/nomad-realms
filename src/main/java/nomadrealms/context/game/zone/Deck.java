package nomadrealms.context.game.zone;

import nomadrealms.context.game.card.WorldCard;

import java.util.ArrayList;
import java.util.List;

public class Deck extends WorldCardZone {

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

	@Override
	public String toString() {
		return "Deck{" +
				"cards=" + cards +
				'}';
	}
}
