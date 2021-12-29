package model.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.CardPlayer;
import model.state.GameState;

public final class UnlockQueueChainEvent extends ChainEvent {

	public UnlockQueueChainEvent(CardPlayer player) {
		super(player);
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardPlayer cardPlayer = state.cardPlayer(player().id());
		cardPlayer.cardDashboard().queue().setLocked(false);
	}

	@Override
	public int priority() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean checkIsDone() {
		return true;
	}

	@Override
	public boolean cancelled() {
		return false;
	}

	@Override
	public boolean shouldDisplay() {
		return false;
	}

}
