package model.card;

import java.util.Iterator;

/**
 * First is top, last is bottom. Full/empty checks must be performed externally.
 * <ul>
 * <li>[Implementation idea]: For card movement animations, we can mark cards as
 * dirty whenever cards are added/removed from card zones. In the visuals they
 * will gradually approach their correct position every frame.
 * </ul>
 * 
 * @author Lunkle
 *
 */
public class CardZone implements Iterable<GameCard> {

	private int maxSize;
	private RandomAccessArrayDeque<GameCard> cards = new RandomAccessArrayDeque<>();

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

	public GameCard card(int index) {
		return cards.get(index);
	}

	public GameCard drawCard(int index) {
		GameCard card = cards.get(index);
		cards.delete(index);
		return card;
	}

	@Override
	public Iterator<GameCard> iterator() {
		return cards.iterator();
	}

}
