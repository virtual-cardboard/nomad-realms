package nomadrealms.context.game.zone;

import nomadrealms.context.game.card.Card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CardZone<T extends Card> {

	protected List<T> cards;

	public CardZone() {
		cards = new ArrayList<>();
	}

	public CardZone<T> addCard(T card) {
		cards.add(card);
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
		cards.remove(card);
	}

	public List<T> getCards() {
		return new ArrayList<>(cards);
	}

}
