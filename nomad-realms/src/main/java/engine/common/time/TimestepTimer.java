package engine.common.time;

/**
 * A runnable timer that ticks at a fixed rate. In the Nengen engine, this class is used to time the tick logic and the
 * rendering logic.
 * <br>
 * <br>
 * This class is not thread-safe.
 * <br>
 * <br>
 * Note that this class only provides local timing. It does not provide any synchronization across networked systems.
 * <br>
 * To synchronize across networked systems, each process should have its own {@link TimestepTimer}, and the timers
 * should be synchronized with eventual consistency by gradually adjusting the frame rates of each timer. This
 * synchronization is not implemented by this class.
 * <br>
 * <br>
 * This implementation is based on the Gaffer on Games article <a
 * href="http://gafferongames.com/game-physics/fix-your-timestep/">"Fix Your Timestep!"</a> by Glenn Fiedler.
 *
 * @author Lunkle
 */
public abstract class TimestepTimer implements Runnable {

	/**
	 * Time accumulator.
	 */
	private final TimeAccumulator accumulator;

	private long framesElapsed = 0;

	/**
	 * Initializes a <code>TimestepTimer</code> with the given <code>frameDuration</code>.
	 *
	 * @param frameDuration the target duration of each frame, in milliseconds
	 */
	public TimestepTimer(long frameDuration) {
		this.accumulator = new TimeAccumulator(frameDuration);
	}

	@Override
	public final void run() {
		accumulator.reset();
		try {
			startActions();
			while (!endCondition()) {
				if (accumulator.ready()) {
					update();
					accumulator.tick();
					framesElapsed++;
				}
				Thread.yield();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			endActions();
		}
	}

	/**
	 * Updates the game. This function is called every tick.
	 */
	protected abstract void update();

	/**
	 * This method should be overridden by the game programmer to return true when the timer should stop.
	 *
	 * @return true if the {@link TimestepTimer} should end, false otherwise.
	 */
	protected abstract boolean endCondition();

	/**
	 * This method should be overridden by the game programmer to perform any initialization actions before the timer
	 * starts.
	 */
	protected void startActions() {
	}

	/**
	 * This method should be overridden by the game programmer to perform any cleanup actions after the timer ends.
	 */
	protected void endActions() {
	}

	public void setFrameRate(long frameDuration) {
		accumulator.setFrameDuration(frameDuration);
	}

	public long getFramesElapsed() {
		return framesElapsed;
	}

	/**
	 * Returns the alpha value, which represents the proportion of the frame that has elapsed. It is calculated by
	 * dividing the accumulation by the frame duration.
	 *
	 * @return the alpha value
	 */
	public float alpha() {
		return accumulator.alpha();
	}

}
