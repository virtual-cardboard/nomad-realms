package model.task;

import model.GameState;
import model.actor.CardPlayer;
import model.actor.GameObject;

public abstract class Task {

	private GameObject target;
	private boolean cancelled;
	private boolean paused;

	public abstract void begin(CardPlayer cardPlayer, GameObject target, GameState state);

	/**
	 * Causes the cardPlayer to execute the task.
	 * 
	 * @param cardPlayer
	 * @param state
	 * @return whether the task is now finished
	 */
	public abstract void execute(CardPlayer cardPlayer, GameObject target, GameState state);

	public void pause(CardPlayer cardPlayer, GameObject target, GameState state) {
	}

	public void resume(CardPlayer cardPlayer, GameObject target, GameState state) {
	}

	public GameObject target() {
		return target;
	}

	public final void setTarget(GameObject target) {
		this.target = target;
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

}
