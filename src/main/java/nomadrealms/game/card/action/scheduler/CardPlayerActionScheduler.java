package nomadrealms.game.card.action.scheduler;

import java.util.LinkedList;
import java.util.Queue;

import nomadrealms.game.actor.Actor;
import nomadrealms.game.card.action.Action;
import nomadrealms.game.world.World;

/**
 * An action scheduler that keeps track of the delay between an actor's actions.
 * <br>
 * <br>
 * Owned by an {@link Actor} itself, the action sc it makes sure that between
 * the post-delay of the previous action and
 * the pre-delay of the next action, the actor cannot execute any actions.
 */
public class CardPlayerActionScheduler {

	/**
	 * The previous action that was executed.
	 * <br>
	 * <br>
	 * It applies its post-delay before the next action can be executed.
	 */
	private Action previousAction;

	/**
	 * The current action being executed.
	 * <br>
	 * <br>
	 * If this is not null, then delay counters do not increment. Null if no action
	 * is being executed.
	 */
	private Action currentAction;

	/**
	 * The queue of actions to be executed.
	 * <br>
	 * <br>
	 * It applies its pre-delay before it itself can be executed.
	 */
	private Queue<Action> actionQueue = new LinkedList<>();

	/**
	 * The delay counter for the current action.
	 * <br>
	 * <br>
	 * Resets to 0 when the current action is non-null.
	 */
	private int counter;

	public CardPlayerActionScheduler() {
	}

	public void update(World world) {
		if (currentAction == null && actionQueue.isEmpty()) {
			return;
		}
		if (currentAction == null) {
			int postDelay = previousAction == null ? 0 : previousAction.postDelay();
			int preDelay = actionQueue.isEmpty() ? 0 : actionQueue.peek().preDelay();
			if (counter == postDelay + preDelay) {
				currentAction = actionQueue.poll();
				counter = 0;
			}
			if (counter > postDelay + preDelay) {
				throw new IllegalStateException("Counter exceeded delay. Pre-delay: " + preDelay + ", Post-delay: "
						+ postDelay + ", Counter: " + counter);
			}
			counter++;
		} else {
			if (!currentAction.isComplete()) {
				currentAction.update(world);
				return; // Wait for the action to complete.
			}
			previousAction = currentAction;
			currentAction = null;
		}
	}

	public void reset() {
		counter = 0;
	}

	public void queue(Action action) {
		actionQueue.add(action);
	}

}
