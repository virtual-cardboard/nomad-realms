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

	public void processAll(NomadsGameData data, Queue<GameEvent> networkSync, Queue<GameEvent> visualSync) {
		List<EffectChain> toAdd = new ArrayList<>();
		for (Iterator<EffectChain> iterator = this.iterator(); iterator.hasNext();) {
			EffectChain effectChain = iterator.next();
			// TODO: handle 0 tick effects
			// Process first
			if (effectChain.tickCount() == 0) {
				effectChain.first().process(data.state(), visualSync);
			}
			effectChain.increaseTick();
			if (effectChain.tickCount() >= effectChain.first().processTime()) {
				// Process time ends, poll effect chain
				effectChain.poll();
				iterator.remove();
				effectChain.resetTicks();
				if (!effectChain.isEmpty()) {
					// Sort again
					toAdd.add(effectChain);
				}
			}
		}
		// TODO: pass networkSync as a parameter to ChainEvent.process
		networkSync.addAll(visualSync);
		addAll(toAdd);
	}

}
