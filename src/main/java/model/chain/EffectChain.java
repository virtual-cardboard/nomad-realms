package model.chain;

import static java.lang.Integer.compare;

import java.util.ArrayList;

import event.game.logicprocessing.chain.ChainEvent;

public class EffectChain extends ArrayList<ChainEvent> implements Comparable<EffectChain> {

	private static final long serialVersionUID = 7559231059563102016L;
	private boolean shouldProcess = true;
	private int currentIndex = 0;

	public boolean shouldProcess() {
		return shouldProcess;
	}

	public void setShouldProcess(boolean shouldProcess) {
		this.shouldProcess = shouldProcess;
	}

	public int currentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public void increaseCurrentIndex() {
		currentIndex++;
	}

	public ChainEvent current() {
		return get(currentIndex);
	}

	@Override
	public int compareTo(EffectChain o) {
		return compare(isEmpty() || finished() ? 0 : current().priority(), o.isEmpty() || o.finished() ? 0 : o.current().priority());
	}

	public boolean finished() {
		return currentIndex == size();
	}

	@Override
	public String toString() {
		return "EffectChain currentIndex = " + currentIndex + super.toString();
	}

}
