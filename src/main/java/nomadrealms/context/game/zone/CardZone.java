package nomadrealms.context.game.zone;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import nomadrealms.context.game.card.Card;

public class CardZone<T extends Card> {

	protected List<T> cards;

	@SafeVarargs
	public CardZone(T... cards) {
		this.cards = new ArrayList<>(Arrays.asList(cards));
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

	public int size() {
		return cards.size();
	}

}
