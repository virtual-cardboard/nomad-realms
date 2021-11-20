package model.card;

import static java.lang.Integer.MAX_VALUE;

import java.util.ArrayList;

/**
 * First is top, last is bottom. Full/empty checks must be performed externally.
 * 
 * @author Lunkle
 *
 */
public class CardZone extends ArrayList<GameCard> {

	private static final long serialVersionUID = 8931876490271319458L;

	private int maxSize;

	public CardZone(int maxSize, GameCard... cards) {
		this.maxSize = maxSize;
		for (GameCard card : cards) {
			add(card);
		}
	}

	public CardZone(GameCard... cards) {
		this(MAX_VALUE, cards);
	}

	public void addTop(GameCard card) {
		add(0, card);
	}

	public void addBottom(GameCard card) {
		add(card);
	}

	public GameCard drawTop() {
		return remove(0);
	}

	public GameCard drawBottom() {
		return remove(size() - 1);
	}

	public int indexOf(long id) {
		for (int i = 0; i < size(); i++) {
			GameCard gameCard = get(i);
			if (gameCard.id() == id) {
				return i;
			}
		}
		return -1;
	}

	public boolean full() {
		return size() == maxSize;
	}

	public boolean empty() {
		return isEmpty();
	}

	public GameCard card(int index) {
		return get(index);
	}

	public GameCard draw(int index) {
		return remove(index);
	}

}
