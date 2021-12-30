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
		List<EffectChain> toRemove = new ArrayList<>();
		List<EffectChain> toAdd = new ArrayList<>();
		for (Iterator<EffectChain> iterator = this.iterator(); iterator.hasNext();) {
			EffectChain effectChain = iterator.next();
			if (effectChain.current().cancelled()) {
				// Remove chain if effect cancelled
				effectChain.current().player().removeChain(effectChain);
				toRemove.add(effectChain);
				continue;
			}

			if (effectChain.shouldProcess()) {
				effectChain.current().process(data.nextState(), visualSync);
				effectChain.setShouldProcess(false);
			}

			if (effectChain.current().checkIsDone()) {
				// Process time ends, poll effect chain
				effectChain.increaseCurrentIndex();
				// Start processing cards again
				effectChain.setShouldProcess(true);

				toRemove.add(effectChain);

				if (!effectChain.finished()) {
					// Sort again
					toAdd.add(effectChain);
				}
			}
		}
		removeAll(toRemove);
		addAll(toAdd);
	}

	public ChainHeap copy() {
		ChainHeap copy = new ChainHeap();
		copy.addAll(this);
		return copy;
	}

}
