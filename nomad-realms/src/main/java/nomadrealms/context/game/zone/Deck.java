package nomadrealms.context.game.zone;

import engine.common.math.SerializableRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.event.game.cardzone.event.RestockCardZoneEvent;

public class Deck extends WorldCardZone {

	public Deck() {
		super();
	}

	@Override
	public WorldCardZone addCard(WorldCard card) {
		super.addCard(card);
		card.deck(this);
		return this;
	}

	public void shuffle(SerializableRandom rng) {
		List<WorldCard> newCards = new ArrayList<>();
		while (!cards.isEmpty()) {
			int index = rng.nextInt(cards.size());
			newCards.add(cards.remove(index));
		}
		cards = newCards;
	}

	public void restock(WorldCardZone sharedDiscard, SerializableRandom rng) {
		List<WorldCard> myDiscardedCards = sharedDiscard.getCards().stream()
				.filter(card -> card.deck() == this)
				.collect(Collectors.toList());
		sharedDiscard.removeCards(myDiscardedCards);
		addCards(myDiscardedCards);
		shuffle(rng);
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
	public void reindex() {
		super.reindex();
	}

	@Override
	public String toString() {
		return "Deck{" +
				"cards=" + cards +
				'}';
	}
}
