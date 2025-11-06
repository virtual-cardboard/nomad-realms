package nomadrealms.game.card.action.scheduler;

import java.util.LinkedList;
import java.util.Queue;

import engine.common.math.Vector2f;
import nomadrealms.game.actor.Actor;
import nomadrealms.game.card.action.Action;
import nomadrealms.game.world.World;
import nomadrealms.render.RenderingEnvironment;

/**
 * An action scheduler that keeps track of the delay between an actor's actions.
 * <br>
 * <br>
 * Owned by an {@link Actor} itself, the action sc it makes sure that between the post-delay of the previous action and
 * the pre-delay of the next action, the actor cannot execute any actions.
 */
public class CardPlayerActionScheduler {

	/**
	 * The current action being executed.
	 */
	private Action current;

	/**
	 * The queue of actions to be executed.
	 */
	private Queue<Action> queue = new LinkedList<>();

	/**
	 * The delay counter for the current action.
	 * <br>
	 * <br>
	 * Resets to 0 when the current action is completed.
	 */
	private int counter;

	public CardPlayerActionScheduler() {
	}

	public void update(World world) {
		if (current == null) {
			if (queue.isEmpty()) {
				return;
			} else {
				current = queue.poll();
				current.init(world);
			}
		}
		if (counter < current.preDelay()) {
			counter++;
		} else if (!current.isComplete()) {
			current.update(world);
		} else if (counter < current.preDelay() + current.postDelay()) {
			counter++;
		} else if (counter == current.preDelay() + current.postDelay()) {
			current = null;
			counter = 0;
		} else if (counter > current.preDelay() + current.postDelay()) {
			throw new IllegalStateException("Counter exceeded delay. Pre-delay: " + current.preDelay() + ", Post-delay: "
					+ current.postDelay() + ", Counter: " + counter);
		}
	}

	public void reset() {
		counter = 0;
	}

	public void queue(Action action) {
		queue.add(action);
	}

	public Vector2f getScreenOffset(RenderingEnvironment re, long time) {
		if (current != null) {
			return current.getScreenOffset(re, time);
		}
		return new Vector2f(0, 0);
	}

}
