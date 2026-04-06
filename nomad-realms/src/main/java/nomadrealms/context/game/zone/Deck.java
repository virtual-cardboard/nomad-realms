package nomadrealms.context.game.zone;

import java.util.ArrayList;
import java.util.List;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.event.game.cardzone.event.RestockCardZoneEvent;

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

	public void restock(WorldCardZone discardZone) {
		List<WorldCard> allDiscardCards = discardZone.getCards();
		List<WorldCard> deckCards = new ArrayList<>();
		for (WorldCard card : allDiscardCards) {
			if (card.deck() == this) {
				deckCards.add(card);
				discardZone.removeCard(card);
			}
		}
		addCards(deckCards);
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

	@Override
	public String toString() {
		return "Deck{" +
				"cards=" + cards +
				'}';
	}
}
