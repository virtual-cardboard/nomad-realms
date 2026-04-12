package engine.common.time;

public class TimestampTracker {

	private long lastTime;

	public TimestampTracker() {
		this.lastTime = System.currentTimeMillis();
	}

	public long getElapsed() {
		long currentTime = System.currentTimeMillis();
		long elapsed = currentTime - lastTime;
		lastTime = currentTime;
		return elapsed;
	}

	public void reset() {
		lastTime = System.currentTimeMillis();
	}

}
