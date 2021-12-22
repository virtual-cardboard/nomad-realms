package model.chain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import common.event.GameEvent;
import context.game.NomadsGameData;

public class ChainHeap extends PriorityQueue<EffectChain> {

	private static final long serialVersionUID = 7756504389693280798L;

	public void processAll(NomadsGameData data, Queue<GameEvent> visualSync) {
		List<Object> toRemove = new ArrayList<>();
		List<EffectChain> toAdd = new ArrayList<>();
		for (Iterator<EffectChain> iterator = this.iterator(); iterator.hasNext();) {
			EffectChain effectChain = iterator.next();
			// Handle 0 tick effects
			while (!effectChain.finished() && effectChain.current().processTime() == 0) {
				effectChain.current().process(data.state(), visualSync);
				effectChain.increaseCurrentIndex();
			}
			if (effectChain.finished()) {
				toRemove.add(effectChain);
				continue;
			}
			// Process current
			if (effectChain.tickCount() == 0) {
				effectChain.current().process(data.state(), visualSync);
			}
			effectChain.increaseTick();
			if (effectChain.tickCount() == effectChain.current().processTime()) {
				// Process time ends, poll effect chain
				effectChain.increaseCurrentIndex();
				effectChain.resetTicks();
				if (effectChain.finished()) {
					// Sort again
					toRemove.add(effectChain);
					toAdd.add(effectChain);
				}
			}
		}
		removeAll(toRemove);
		addAll(toAdd);
	}

}
