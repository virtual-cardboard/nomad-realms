package model.card;

import model.GameState;
import model.actor.CardPlayer;
import model.actor.GameObject;

public abstract class Task {

	private GameObject target;

	/**
	 * Causes the cardPlayer to execute the task.
	 * 
	 * @param cardPlayer
	 * @param state
	 * @return whether the task is now finished
	 */
	public abstract void execute(CardPlayer cardPlayer, GameObject target, GameState state);

	public GameObject target() {
		return target;
	}

	public final void setTarget(GameObject target) {
		this.target = target;
	}

	public abstract boolean isDone();

}
