package model.card;

import model.GameState;
import model.actor.CardPlayer;

public abstract class Task {

	private boolean done;

	public final void doExecute(CardPlayer cardPlayer, GameState state) {
		done = execute(cardPlayer, state);
	}

	/**
	 * Causes the cardPlayer to execute the task.
	 * 
	 * @param cardPlayer
	 * @param state
	 * @return whether the task is now finished
	 */
	public abstract boolean execute(CardPlayer cardPlayer, GameState state);

	public boolean isDone() {
		return done;
	}

}
