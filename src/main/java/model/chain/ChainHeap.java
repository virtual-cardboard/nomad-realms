package model.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import context.game.NomadsGameData;
import engine.common.ContextQueues;
import model.chain.event.ChainEvent;
import model.state.GameState;

public class ChainHeap extends PriorityQueue<EffectChain> {

	private static final long serialVersionUID = 7756504389693280798L;

	public List<ChainEvent> processAll(long tick, NomadsGameData data, ContextQueues contextQueues) {
		List<ChainEvent> chainEvents = new ArrayList<>(5);
		List<EffectChain> toRemove = new ArrayList<>();
		List<EffectChain> toRetain = new ArrayList<>();
		GameState currentState = data.nextState();
		for (EffectChain chain : this) {
			if (chain.shouldProcess()) {
				if (chain.first().cancelled(currentState)) {
					// Unlock the queue
					if (chain.createdFromCard() && !chain.unlockedQueue()) {
						chain.cardPlayerID().getFrom(currentState).cardDashboard().queue().setLocked(false);
						chain.setUnlockedQueue(true);
					}
					// Remove chain if effect cancelled
					toRemove.add(chain);
					continue;
				}
				chainEvents.add(chain.first());
				chain.setShouldProcess(false);
			}

			if (chain.first().cancelled(currentState) || chain.first().checkIsDone(currentState)) {
				// Process time ends, poll effect chain
				chain.poll();
				// Start processing cards again
				chain.setShouldProcess(true);

				// Unlock queue if needed
				if (chain.createdFromCard() && !chain.unlockedQueue() && chain.numWheneverEvents() == 0) {
					chain.cardPlayerID().getFrom(currentState).cardDashboard().queue().setLocked(false);
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
		return chainEvents;
	}

	public ChainHeap copy() {
		ChainHeap copy = new ChainHeap();
		copy.addAll(this);
		return copy;
	}

}
