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
		List<EffectChain> toRetain = new ArrayList<>();
		GameState state = data.nextState();
		for (EffectChain chain : this) {
			if (chain.shouldProcess()) {
				if (chain.first().cancelled(state)) {
					// Unlock the queue
					if (chain.createdFromCard() && !chain.unlockedQueue()) {
						state.cardPlayer(chain.cardPlayerID()).cardDashboard().queue().setLocked(false);
						chain.setUnlockedQueue(true);
					}
					// Remove chain if effect cancelled
					toRemove.add(chain);
					continue;
				}
				chain.first().process(state, visualSync);
				chain.setShouldProcess(false);
			}

			if (chain.first().cancelled(state) || chain.first().checkIsDone(state)) {
				// Process time ends, poll effect chain
				chain.poll();
				// Start processing cards again
				chain.setShouldProcess(true);

				// Unlock queue if needed
				if (chain.createdFromCard() && !chain.unlockedQueue() && chain.numWheneverEvents() == 0) {
					state.cardPlayer(chain.cardPlayerID()).cardDashboard().queue().setLocked(false);
					chain.setUnlockedQueue(true);
				}

				if (chain.isEmpty()) {
					toRemove.add(chain);
				} else {
					// Sort again
					toRetain.add(chain);
				}
			}
		}
		removeAll(toRemove);
		removeAll(toRetain);
		addAll(toRetain);
	}

	public ChainHeap copy() {
		ChainHeap copy = new ChainHeap();
		copy.addAll(this);
		return copy;
	}

}
