package engine.common.time;

import static java.lang.System.currentTimeMillis;

/**
 * A class that encapsulates time accumulation.
 * <br>
 * Theoretically, this class could be entirely replaced by a float, but it is useful to have a class to encapsulate the
 * logic of time accumulation.
 * <br>
 * <br>
 * This class is not thread-safe.
 * <br>
 * <br>
 * Note that this class only provides local time accumulation. It does not provide any synchronization across networked
 * systems.
 *
 * @author Lunkle
 */
public class TimeAccumulator {

	/**
	 * The amount of time that has been accumulated, in milliseconds.
	 */
	private long accumulation = 0;

	/**
	 * The amount of time that has passed since the last update, in milliseconds.
	 */
	private long lastTimeSyncTime;

	/**
	 * The duration of a frame, in milliseconds.
	 */
	private long frameDuration;

	/**
	 * Creates a new <code>TimeAccumulator</code> with the given
	 * <code>frameDuration</code>.
	 *
	 * @param frameDuration the duration of a frame, in milliseconds
	 */
	public TimeAccumulator(long frameDuration) {
		lastTimeSyncTime = currentTimeMillis();
		this.frameDuration = frameDuration;
	}

	private void syncTime() {
		long newTime = currentTimeMillis();
		accumulation += newTime - lastTimeSyncTime;
		lastTimeSyncTime = newTime;
	}

	/**
	 * Updates the time accumulator and returns the amount of time that has been accumulated, in milliseconds.
	 *
	 * @return the amount of time that has been accumulated, in milliseconds
	 */
	public long accumulation() {
		syncTime();
		return accumulation;
	}

	/**
	 * Decreases the accumulation by the frame duration.
	 */
	public void tick() {
		accumulation = accumulation - frameDuration;
		if (accumulation < 0) {
			throw new IllegalStateException("Time accumulation is negative after tick down. Please check tick down logic.");
		}
	}

	/**
	 * Sets the frame duration.
	 *
	 * @param frameDuration the duration of a frame, in milliseconds
	 */
	public void setFrameDuration(long frameDuration) {
		this.frameDuration = frameDuration;
	}

	/**
	 * The alpha value is used to represent the proportion of the frame that has elapsed. It is calculated by dividing
	 * the accumulation by the frame duration.
	 *
	 * @return the alpha value
	 */
	public float alpha() {
		return (float) accumulation / frameDuration;
	}

	/**
	 * Resets the time accumulator.
	 * <br>
	 * Note that this method does not reset the frame duration.
	 */
	public void reset() {
		syncTime();
		accumulation = 0;
	}

	/**
	 * Signals whether the time accumulator is ready to tick down. This method returns true if the accumulation is
	 * greater than or equal to the frame duration.
	 *
	 * @return true if the accumulation is greater than or equal to the frame duration, false otherwise
	 */
	public boolean ready() {
		syncTime();
		return accumulation >= frameDuration;
	}

}
