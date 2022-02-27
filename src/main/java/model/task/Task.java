package model.task;

import model.GameObject;
import model.id.CardPlayerID;
import model.id.ID;
import model.id.TaskID;
import model.state.GameState;

public abstract class Task extends GameObject {

	protected ID targetID;
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
	public abstract void execute(CardPlayerID playerID, GameState state);

	public void pause(CardPlayerID playerID, GameState state) {
	}

	public void resume(CardPlayerID playerID, GameState state) {
	}

	public ID targetID() {
		return targetID;
	}

	@Override
	public TaskID id() {
		return new TaskID(id);
	}

	public final void setTarget(ID targetID) {
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

	@Override
	public abstract Task copy();

	@Override
	public void addTo(GameState state) {
		state.tasks().put(id, this);
	}

	public <T extends Task> T copyTo(T task) {
		task.targetID = targetID;
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
