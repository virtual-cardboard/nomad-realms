package model.chain;

import static java.lang.Integer.compare;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.chain.event.ChainEvent;
import model.id.CardPlayerId;

public class EffectChain implements Comparable<EffectChain> {

	private boolean shouldProcess = true;

	private List<ChainEvent> whenever = new ArrayList<>(1);
	private List<ChainEvent> after = new ArrayList<>(0);

	private CardPlayerId cardPlayerID;
	private boolean createdFromCard;
	private boolean unlockedQueue = false;

	public EffectChain() {
		createdFromCard = false;
	}

	public EffectChain(CardPlayerId cardPlayerID) {
		this.cardPlayerID = cardPlayerID;
		createdFromCard = true;
	}

	public boolean shouldProcess() {
		return shouldProcess;
	}

	public void setShouldProcess(boolean shouldProcess) {
		this.shouldProcess = shouldProcess;
	}

	public ChainEvent get(int i) {
		if (i >= whenever.size()) {
			return after.get(i - whenever.size());
		}
		return whenever.get(i);
	}

	public ChainEvent first() {
		if (isEmpty()) {
			return null;
		} else if (whenever.isEmpty()) {
			return after.get(0);
		}
		return whenever.get(0);
	}

	public ChainEvent poll() {
		if (isEmpty()) {
			return null;
		} else if (whenever.isEmpty()) {
			return after.remove(0);
		}
		return whenever.remove(0);
	}

	@Override
	public int compareTo(EffectChain o) {
		return compare(isEmpty() ? 0 : first().priority(), o.isEmpty() ? 0 : o.first().priority());
	}

	@Override
	public String toString() {
		return "EffectChain size=" + size() + " first=" + first();
	}

	public int size() {
		return whenever.size() + after.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public void addWheneverEvent(ChainEvent event) {
		whenever.add(event);
	}

	public void addAfterEvent(ChainEvent event) {
		after.add(event);
	}

	public int numWheneverEvents() {
		return whenever.size();
	}

	public int numAfterEvents() {
		return after.size();
	}

	public CardPlayerId cardPlayerID() {
		return cardPlayerID;
	}

	public boolean createdFromCard() {
		return createdFromCard;
	}

	public boolean unlockedQueue() {
		return unlockedQueue;
	}

	public void setUnlockedQueue(boolean unlockedQueue) {
		this.unlockedQueue = unlockedQueue;
	}

	public void addAllWhenever(Collection<ChainEvent> wheneverEvents) {
		whenever.addAll(wheneverEvents);
	}

}
