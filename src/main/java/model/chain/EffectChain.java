package model.chain;

import static java.lang.Integer.compare;

import java.util.ArrayDeque;

import event.game.logicprocessing.chain.ChainEvent;

public class EffectChain extends ArrayDeque<ChainEvent> implements Comparable<EffectChain> {

	private static final long serialVersionUID = 7580376132817245970L;
	private int tickCount = 0;

	public int tickCount() {
		return tickCount;
	}

	public void increaseTick() {
		tickCount++;
	}

	public void resetTicks() {
		tickCount = 0;
	}

	public ChainEvent first() {
		return super.peek();
	}

	@Override
	public int compareTo(EffectChain o) {
		return compare(isEmpty() ? 0 : peek().priority(), o.isEmpty() ? 0 : o.peek().priority());
	}

}
