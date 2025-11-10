package nomadrealms.context.game.card.action.scheduler.lock;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.action.Action;

/**
 * An action scheduler that keeps track of the delay between an actor's actions.
 * <br><br>
 * Owned by an {@link Actor} itself, it makes sure that between the post-delay of the previous action and the pre-delay
 * of the next action, the actor cannot execute any actions.
 * <br><br>
 * Note that actions themselves may have their own delays.
 */
public interface ActionScheduler {

	/**
	 * Updates the action scheduler.
	 * <br><br>
	 * This should be called every frame. If between the post-delay of the previous action and the pre-delay of the next
	 * action, this method stalls. Otherwise, it will execute the next action.
	 * <br><br>
	 * Note that actions themselves may have their own delays.
	 */
	public void update();

	/**
	 * Schedules the given action to be executed eventually.
	 *
	 * @param action the action to schedule
	 */
	public void schedule(Action action);

}
