package engine.common.time;

/**
 * A class that keeps track of the frames per second (FPS) over a moving window of frames.
 */
public class FPSCounter {

	private long lastTime;
	private final float[] frameTimes;
	private int index = 0;
	private int count = 0;
	private float totalTime = 0;
	private float averageFPS;

	/**
	 * Creates a new FPSCounter with the specified moving window size.
	 *
	 * @param windowSize the number of frames to average the FPS over
	 */
	public FPSCounter(int windowSize) {
		this.frameTimes = new float[windowSize];
	}

	/**
	 * Updates the FPS counter. This should be called once every frame.
	 */
	public void update() {
		long currentTime = System.nanoTime();
		if (lastTime == 0) {
			lastTime = currentTime;
			return;
		}
		float deltaTime = (currentTime - lastTime) / 1_000_000_000f;
		lastTime = currentTime;

		if (count == frameTimes.length) {
			totalTime -= frameTimes[index];
		}
		frameTimes[index] = deltaTime;
		totalTime += deltaTime;

		index = (index + 1) % frameTimes.length;
		if (count < frameTimes.length) {
			count++;
		}

		if (totalTime > 0) {
			averageFPS = count / totalTime;
		}
	}

	/**
	 * Returns the average FPS over the moving window.
	 *
	 * @return the average FPS
	 */
	public float getFPS() {
		return averageFPS;
	}

}
