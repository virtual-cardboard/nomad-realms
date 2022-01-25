package model.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import common.event.GameEvent;
import context.game.NomadsGameData;
import model.state.GameState;

public class ChainHeap extends PriorityQueue<EffectChain> {

	private static final long serialVersionUID = 7756504389693280798L;

	public void processAll(NomadsGameData data, Queue<GameEvent> visualSync) {
		List<EffectChain> toRemove = new ArrayList<>();
		List<EffectChain> toAdd = new ArrayList<>();
		GameState state = data.nextState();
		for (EffectChain effectChain : this) {
			if (effectChain.first().cancelled(state)) {
				// Remove chain if effect cancelled
				state.cardPlayer(effectChain.first().playerID()).removeChain(effectChain);
				toRemove.add(effectChain);
				continue;
			}

			if (effectChain.shouldProcess()) {
				effectChain.first().process(state, visualSync);
				effectChain.setShouldProcess(false);
			}

			if (effectChain.first().checkIsDone()) {
				// Process time ends, poll effect chain
				effectChain.poll();
				// Start processing cards again
				effectChain.setShouldProcess(true);

				toRemove.add(effectChain);

				if (!effectChain.isEmpty()) {
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
