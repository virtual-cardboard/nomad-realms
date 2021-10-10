package model.card;

import java.util.ArrayDeque;

/**
 * First is top, last is bottom. Full/empty checks must be performed externally.
 * 
 * @author Lunkle
 *
 */
public class CardZone {

	private int maxSize;
	private ArrayDeque<GameCard> cards = new ArrayDeque<>();

	public CardZone(int maxSize, GameCard... cards) {
		this.maxSize = maxSize;
		for (GameCard card : cards) {
			addBottom(card);
		}
	}

	public CardZone(GameCard... cards) {
		this(Integer.MAX_VALUE, cards);
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

	public boolean full() {
		return size() == maxSize;
	}

	public boolean empty() {
		return size() == 0;
	}

}
