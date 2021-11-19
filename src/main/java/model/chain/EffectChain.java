package model.chain;

import static java.lang.Integer.compare;

import event.game.logicprocessing.chain.ChainEvent;
import model.card.RandomAccessArrayDeque;

public class EffectChain extends RandomAccessArrayDeque<ChainEvent> implements Comparable<EffectChain> {

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

	@Override
	public int compareTo(EffectChain o) {
		return compare(isEmpty() ? 0 : peek().priority(), o.isEmpty() ? 0 : o.peek().priority());
	}

}
