package model.card;

import static java.lang.Integer.MAX_VALUE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * First is top, last is bottom. Full/empty checks must be performed externally.
 * 
 * @author Lunkle
 *
 */
public class CardZone extends ArrayList<WorldCard> {

	private static final long serialVersionUID = 8931876490271319458L;

	private int maxSize;

	public CardZone(int maxSize, WorldCard... cards) {
		this.maxSize = maxSize;
		for (WorldCard card : cards) {
			add(card);
		}
	}

	public CardZone(WorldCard... cards) {
		this(MAX_VALUE, cards);
	}

	public void addTop(WorldCard card) {
		add(0, card);
	}

	public void addBottom(WorldCard card) {
		add(card);
	}

	public WorldCard drawTop() {
		return remove(0);
	}

	public WorldCard drawBottom() {
		return remove(size() - 1);
	}

	/**
	 * Shuffles the cardzone using exactly one random long from the specified
	 * random.
	 * 
	 * @param random the {@link Random} to use
	 */
	public void shuffle(Random random) {
		Collections.shuffle(this, new Random(random.nextLong()));
	}

	public int indexOf(long id) {
		for (int i = 0; i < size(); i++) {
			WorldCard gameCard = get(i);
			if (gameCard.longID() == id) {
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

	public WorldCard card(int index) {
		return get(index);
	}

	public WorldCard draw(int index) {
		return remove(index);
	}

	public CardZone copy() {
		CardZone copy = new CardZone(maxSize);
		for (int i = 0; i < size(); i++) {
			copy.add(card(i).copy());
		}
		return copy;
	}

	public boolean notEmpty() {
		return !isEmpty();
	}

}
