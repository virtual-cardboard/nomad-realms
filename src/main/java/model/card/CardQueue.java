package model.card;

import static java.lang.Integer.MAX_VALUE;

import java.util.Iterator;

import event.game.CardPlayedEvent;

public class CardQueue implements Iterable<CardPlayedEvent> {

	private int maxSize;
	private RandomAccessArrayDeque<CardPlayedEvent> playedCards = new RandomAccessArrayDeque<>();
	private int tickCount = 0;

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

	public CardPlayedEvent first() {
		return playedCards.getFirst();
	}

	public void append(CardPlayedEvent event) {
		playedCards.addLast(event);
	}

	public CardPlayedEvent poll() {
		return playedCards.removeFirst();
	}

	public boolean delete(int index) {
		if (index == 0) {
			resetTicks();
		}
		return playedCards.delete(index);
	}

	public int size() {
		return playedCards.size();
	}

	public boolean full() {
		return size() == maxSize;
	}

	public boolean empty() {
		return size() == 0;
	}

	public CardPlayedEvent event(int index) {
		return playedCards.get(index);
	}

	@Override
	public Iterator<CardPlayedEvent> iterator() {
		return playedCards.iterator();
	}

}
