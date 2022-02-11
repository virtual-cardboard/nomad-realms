package model.task;

import model.state.GameState;

public abstract class Task {

	protected long targetID;
	protected boolean cancelled;
	protected boolean paused = true;

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

	public <T extends Task> T copyTo(T task) {
		task.targetID = targetID;
		task.cancelled = cancelled;
		task.paused = paused;
		return task;
	}

	public abstract String name();

}
