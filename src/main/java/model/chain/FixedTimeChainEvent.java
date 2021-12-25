package model.chain;

import model.actor.CardPlayer;

public abstract class FixedTimeChainEvent extends ChainEvent {

	private int spentTime;

	public FixedTimeChainEvent(CardPlayer player) {
		super(player);
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
	public boolean cancelled() {
		return player().isDead();
	}

	@Override
	public boolean shouldDisplay() {
		return true;
	}

}
