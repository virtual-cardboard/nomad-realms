package model.chain;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import common.event.GameEvent;
import context.game.NomadsGameData;

public class ChainHeap extends PriorityQueue<EffectChain> {

	private static final long serialVersionUID = 7756504389693280798L;

	public void processAll(NomadsGameData data, Queue<GameEvent> sync) {
		for (Iterator<EffectChain> iterator = this.iterator(); iterator.hasNext();) {
			EffectChain effectChain = iterator.next();
			effectChain.increaseTick();
			if (effectChain.tickCount() == effectChain.peek().processTime()) {
				effectChain.poll().process(data.state(), sync);
			}
			if (effectChain.isEmpty()) {
				iterator.remove();
			}
		}
	}

}
