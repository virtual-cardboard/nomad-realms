package model.card;

import java.util.ArrayDeque;

/**
 * First is top, last is bottom.
 * 
 * @author Lunkle
 *
 */
public class CardZone {

	private ArrayDeque<GameCard> cards = new ArrayDeque<>();

	public CardZone(GameCard... cards) {
		for (GameCard card : cards) {
			addBottom(card);
		}
	}

	public void addTop(GameCard card) {
		cards.addFirst(card);
	}

	public void addBottom(GameCard card) {
		cards.addLast(card);
	}

	public GameCard drawTop() {
		return cards.removeFirst();
	}

	public GameCard drawBottom() {
		return cards.removeLast();
	}

	public int size() {
		return cards.size();
	}

}
