package model.task;

import derealizer.Derealizable;
import model.id.CardPlayerId;
import model.id.Id;
import model.state.GameState;

public abstract class Task implements Derealizable {

	protected Id targetId;
	protected boolean cancelled;
	protected boolean paused = false;
	protected boolean done = false;

	public Task() {
	}

	/**
	 * Causes the cardPlayer to execute the task.
	 *
	 * @param cardPlayer
	 * @param state
	 * @return whether the task is now finished
	 */
	public abstract void execute(CardPlayerId playerID, GameState state);

	public void pause(CardPlayerId playerID, GameState state) {
	}

	public void resume(CardPlayerId playerID, GameState state) {
	}

	public Id targetID() {
		return targetId;
	}

	public final void setTarget(Id targetId) {
		this.targetId = targetId;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

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
		task.targetId = targetId;
		task.cancelled = cancelled;
		task.paused = paused;
		return task;
	}

	public abstract String name();

}
