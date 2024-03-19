package nomadrealms.card.zone;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.card.card.Card;
import nomadrealms.card.card.GameCard;

public class CardZone<T extends Card> {

	protected List<T> cards;

	public CardZone() {
		cards = new ArrayList<>();
	}

	public void addCard(T card) {
		cards.add(card);
	}

	public void removeCard(T card) {
		cards.remove(card);
	}

	public List<T> getCards() {
		return new ArrayList<>(cards);
	}

}
