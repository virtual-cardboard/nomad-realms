package nomadrealms.game.card.action.scheduler;

import nomadrealms.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.game.card.action.Action;

/**
 * An action scheduler that keeps track of the delay between a body part's actions.
 * <br><br>
 * Owned by an {@link CardPlayerActionScheduler}, it makes sure that between the post-delay of the previous action and
 * the pre-delay of the next action, the body part cannot execute any actions.
 */
public class AppendageActionScheduler {

	private final Appendage appendage;

	/**
	 * The previous action that was executed.
	 * <br><br>
	 * It applies its post-delay before the next action can be executed.
	 */
	private Action previousAction;

	/**
	 * The current action being executed.
	 * <br><br>
	 * If this is not null, then delay counters do not increment. Null if no action is being executed.
	 */
	private Action currentAction;

	/**
	 * The next action to be executed.
	 * <br><br>
	 * It applies its pre-delay before it itself can be executed.
	 */
	private Action nextAction;

	/**
	 * The delay counter for the current action.
	 * <br><br>
	 * Resets to 0 when the current action is non-null.
	 */
	private int counter;

	/**
	 * Constructs an action scheduler for the given appendage.
	 *
	 * @param appendage the appendage to schedule actions for
	 */
	public AppendageActionScheduler(Appendage appendage) {
		this.appendage = appendage;
	}

	public void update() {
		if (currentAction != null) {
			if (!currentAction.isComplete()) {
				return;
			}
			previousAction = currentAction;
			currentAction = null;
		} else {
			counter++;
			int postDelay = previousAction == null ? 0 : previousAction.postDelay();
			int preDelay = nextAction == null ? 0 : nextAction.preDelay();
			if (counter == postDelay + postDelay) {
				currentAction = nextAction;
				nextAction = null;
				counter = 0;
			}
			if (counter > postDelay + preDelay) {
				throw new IllegalStateException("Counter exceeded delay.");
			}
		}
	}

	public void reset() {
		counter = 0;
	}

	public void setNextAction(Action action) {
		nextAction = action;
	}

	public Appendage appendage() {
		return appendage;
	}

}
