package model.card;

import static java.lang.Integer.MAX_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	public void setLocked(boolean locked) {
		this.locked = locked;
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

	public List<WorldCard> toCardZone(GameState state) {
		return this.stream().map((event) -> event.cardID().getFrom(state))
				.collect(Collectors.toList());
	}

	public CardQueue copy() {
		CardQueue copy = new CardQueue(maxSize);
		copy.addAll(this);
		copy.tickCount = tickCount;
		copy.locked = locked;
		return copy;
	}

}
