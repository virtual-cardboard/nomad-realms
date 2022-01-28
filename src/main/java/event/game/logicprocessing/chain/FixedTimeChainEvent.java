package event.game.logicprocessing.chain;

import model.state.GameState;

public abstract class FixedTimeChainEvent extends ChainEvent {

	private int spentTime;

	public FixedTimeChainEvent(long playerID) {
		super(playerID);
	}

	/**
	 * @return The time it takes to process this event, in ticks
	 */
	public abstract int processTime();

	@Override
	public boolean checkIsDone() {
		spentTime++;
		return spentTime == processTime();
	}

	@Override
	public boolean cancelled(GameState state) {
		return state.actor(playerID()).shouldRemove();
	}

	@Override
	public boolean shouldDisplay() {
		return true;
	}

}
