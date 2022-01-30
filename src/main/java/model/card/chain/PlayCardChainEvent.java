package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.state.GameState;

public final class PlayCardChainEvent extends ChainEvent {

	private long cardID;

	public PlayCardChainEvent(long playerID, long cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
	}

	@Override
	public int priority() {
		return Integer.MAX_VALUE - 1;
	}

	@Override
	public boolean checkIsDone() {
		return true;
	}

	@Override
	public boolean cancelled(GameState state) {
		return false;
	}

	@Override
	public boolean shouldDisplay() {
		return false;
	}

	public long cardID() {
		return cardID;
	}

}
