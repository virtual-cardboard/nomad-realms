package model.card;

import model.GameState;
import model.actor.CardPlayer;

public abstract class Task {

	/**
	 * Causes the cardPlayer to execute the task.
	 * 
	 * @param cardPlayer
	 * @param state
	 * @return whether the task is now finished
	 */
	public abstract void execute(CardPlayer cardPlayer, GameState state);

	public abstract boolean isDone();

}
