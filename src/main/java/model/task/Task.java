package model.task;

import model.state.GameState;

public abstract class Task {

	private long targetID;
	private boolean cancelled;
	private boolean paused = true;

	/**
	 * Causes the cardPlayer to execute the task.
	 * 
	 * @param cardPlayer
	 * @param state
	 * @return whether the task is now finished
	 */
	public abstract void execute(long playerID, GameState state);

	public void pause(long playerID, GameState state) {
	}

	public void resume(long playerID, GameState state) {
	}

	public long targetID() {
		return targetID;
	}

	public final void setTarget(long targetID) {
		this.targetID = targetID;
	}

	public abstract boolean isDone();

	public boolean cancelled() {
		return cancelled;
	}

	public void cancel() {
		this.cancelled = true;
	}

	public boolean paused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public abstract Task copy();

}
