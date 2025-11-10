package engine.visuals.constraint.timed;

import static java.lang.System.currentTimeMillis;

import engine.visuals.constraint.Constraint;

/**
 * A constraint that changes its value over time.
 *
 * @author Lunkle
 */
public class TimedConstraint implements Constraint {

	private long startTime = -1;

	public TimedConstraint() {
	}

	/**
	 * Activates the timed constraint, starting its internal timer. If not explicitly activated, the timer starts on
	 * the first call to {@link #get()}. Calling this method again will reset the timer.
	 */
	public TimedConstraint activate() {
		startTime = currentTimeMillis();
		return this;
	}

	@Override
	public final float get() {
		if (startTime == -1) {
			startTime = currentTimeMillis();
		}
		return currentTimeMillis() - startTime;
	}

	public static TimedConstraint time() {
		return new TimedConstraint();
	}

}
