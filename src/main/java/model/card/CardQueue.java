package model.card;

import static java.lang.Integer.MAX_VALUE;

import java.util.ArrayList;

import event.game.logicprocessing.CardPlayedEvent;
import model.state.GameState;

public class CardQueue extends ArrayList<CardPlayedEvent> {

	private static final long serialVersionUID = -5224470209421169638L;

	private int maxSize;
	private int tickCount = 0;

	private boolean locked;

	public CardQueue(int maxSize, CardPlayedEvent... events) {
		this.maxSize = maxSize;
		for (CardPlayedEvent event : events) {
			append(event);
		}
	}

	public CardQueue(CardPlayedEvent... events) {
		this(MAX_VALUE, events);
	}

	public int tickCount() {
		return tickCount;
	}

	public void increaseTick() {
		tickCount++;
	}

	public void resetTicks() {
		tickCount = 0;
	}

	/**
	 * @return if this queue can currently be processed
	 */
	public boolean locked() {
		return locked;
	}

	public void setLocked(boolean waiting) {
		this.locked = waiting;
	}

	public CardPlayedEvent first() {
		return get(0);
	}

	public void append(CardPlayedEvent event) {
		add(event);
	}

	public CardPlayedEvent poll() {
		resetTicks();
		return remove(0);
	}

	public CardPlayedEvent delete(int index) {
		if (index == 0) {
			resetTicks();
		}
		return remove(index);
	}

	public boolean full() {
		return size() == maxSize;
	}

	public boolean empty() {
		return isEmpty();
	}

	public CardPlayedEvent event(int index) {
		return get(index);
	}

	/**
	 * @param state
	 * @return A new CardQueue consisting of copies of this queue's
	 *         {@link CardPlayedEvent CardPlayedEvents}
	 * 
	 */
	public CardQueue deepCopy(GameState state) {
		CardQueue copy = new CardQueue(maxSize);
		CardQueue cardQueue = this;
		for (int i = 0; i < cardQueue.size(); i++) {
			CardPlayedEvent cpe = cardQueue.get(i);
			copy.add(cpe.copy(state));
		}
		return copy;
	}

}
