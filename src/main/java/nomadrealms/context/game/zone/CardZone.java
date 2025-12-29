package nomadrealms.context.game.zone;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import nomadrealms.context.game.card.Card;
import nomadrealms.event.game.cardzone.CardZoneEventChannel;
import nomadrealms.event.game.cardzone.event.SurfaceCardEvent;

public class CardZone<T extends Card> {

	protected List<T> cards;

	protected CardZoneEventChannel<T> events = new CardZoneEventChannel<>();

	@SafeVarargs
	public CardZone(T... cards) {
		this.cards = new ArrayList<>(asList(cards));
	}

	public CardZone<T> addCard(T card) {
		List<T> newCards = getCards();
		newCards.add(card);
		cards = newCards;
		return this;
	}

	public void addCards(Collection<T> cards) {
		for (T card : cards) {
			addCard(card);
		}
	}

	public void addCards(Stream<T> cards) {
		addCards(cards.collect(toList()));
	}

	public void removeCard(T card) {
		List<T> newCards = getCards();
		newCards.remove(card);
		cards = newCards;
	}

	public List<T> getCards() {
		return new ArrayList<>(cards);
	}

	public int size() {
		return cards.size();
	}

	public void surface(T card) {
		List<T> newCards = getCards();
		newCards.remove(card);
		newCards.add(0, card);
		cards = newCards;
		events.send(new SurfaceCardEvent<T>(card));
	}

	public CardZoneEventChannel<T> events() {
		return events;
	}
}
