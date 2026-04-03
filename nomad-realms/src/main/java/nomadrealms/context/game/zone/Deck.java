package nomadrealms.context.game.zone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nomadrealms.context.game.card.WorldCard;
import nomadrealms.event.game.cardzone.event.RestockCardZoneEvent;

public class Deck extends WorldCardZone<WorldCard> {

	private final WorldCardZone<WorldCard> discardZone = new WorldCardZone<>();
	private int totalCards = 0;

	public Deck() {
		super();
	}

	@Override
	public Deck addCard(WorldCard card) {
		super.addCard(card);
		if (!card.ephemeral()) {
			totalCards++;
		}
		return this;
	}

	@Override
	public void addCards(Collection<WorldCard> cards) {
		for (WorldCard card : cards) {
			addCard(card);
		}
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
		// Use super.addCard to avoid incrementing totalCards
		for (WorldCard card : discardCards) {
			super.addCard(card);
		}
		shuffle();
		events.send(new RestockCardZoneEvent<>(this));
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

	public WorldCardZone<WorldCard> discardZone() {
		return discardZone;
	}

	public int totalCards() {
		return totalCards;
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
				", totalCards=" + totalCards +
				'}';
	}
}
