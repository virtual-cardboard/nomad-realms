package model.task;

import model.GameObject;
import model.id.CardPlayerId;
import model.id.Id;
import model.id.TaskId;
import model.state.GameState;

public abstract class Task extends GameObject {

	protected Id targetId;
	protected boolean cancelled;
	protected boolean paused = true;

	public Task() {
	}

	public Task(long id) {
		super(id);
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

	@Override
	public TaskId id() {
		return new TaskId(id);
	}

	public final void setTarget(Id targetId) {
		this.targetId = targetId;
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

	@Override
	public abstract Task copy();

	@Override
	public void addTo(GameState state) {
		state.tasks().put(id, this);
	}

	public <T extends Task> T copyTo(T task) {
		task.targetId = targetId;
		task.cancelled = cancelled;
		task.paused = paused;
		return task;
	}

	public abstract String name();

	@Override
	public String description() {
		return "Task: " + name() + " ID: " + id;
	}

}
