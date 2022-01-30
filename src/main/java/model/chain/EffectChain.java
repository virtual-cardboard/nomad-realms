package model.chain;

import static java.lang.Integer.compare;

import java.util.ArrayList;

import model.card.chain.ChainEvent;

public class EffectChain extends ArrayList<ChainEvent> implements Comparable<EffectChain> {

	private static final long serialVersionUID = 4000287295039566772L;
	private boolean shouldProcess = true;

	public boolean shouldProcess() {
		return shouldProcess;
	}

	public void setShouldProcess(boolean shouldProcess) {
		this.shouldProcess = shouldProcess;
	}

	public ChainEvent first() {
		return get(0);
	}

	public ChainEvent poll() {
		return remove(0);
	}

	@Override
	public int compareTo(EffectChain o) {
		return compare(isEmpty() ? 0 : first().priority(), o.isEmpty() ? 0 : o.first().priority());
	}

	@Override
	public String toString() {
		return "EffectChain size=" + size() + " first=" + first();
	}

}
