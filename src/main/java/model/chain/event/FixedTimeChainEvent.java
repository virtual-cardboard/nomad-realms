package model.chain.event;

import model.id.CardPlayerId;
import model.state.GameState;

public abstract class FixedTimeChainEvent extends ChainEvent {

	private int spentTime;

	public FixedTimeChainEvent(CardPlayerId playerID) {
		super(playerID);
	}

	/**
	 * @return The time it takes to process this event, in ticks
	 */
	public abstract int processTime();

	@Override
	public boolean checkIsDone(GameState state) {
		return spentTime++ == processTime();
	}

	@Override
	public boolean cancelled(GameState state) {
		return playerID().getFrom(state).shouldRemove();
	}

	@Override
	public boolean shouldDisplay() {
		return true;
	}

}
