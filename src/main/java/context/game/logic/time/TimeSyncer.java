package context.game.logic.time;

public class TimeSyncer {

	/**
	 * The time offset that we want to reduce to 0
	 */
	private long timeOffset;
	private float currentFrameRate = 10;
	private float finalFrameRate = 10;

	public TimeSyncer(long timeOffset) {
		this.timeOffset = timeOffset;
	}

	public long timeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(long timeOffset) {
		this.timeOffset = timeOffset;
	}

	public static void main(String[] args) {
		long clientTime = 2354;
		long serverTime = 2657;
		long clientRate = 100;
		long serverRate = 100;
		for (int i = 0; i < 1000; i++) {
			System.out.println("ClientTime = " + clientTime + " ServerTime = " + serverTime);
			if (clientTime > serverTime) {
				clientRate = serverTime + serverRate - clientTime;
			} else if (clientTime < serverTime) {
				clientRate = serverRate + (serverTime + serverRate - clientTime) / 10;
			} else {
				clientRate = serverRate;
			}
			clientTime += clientRate;
			serverTime += serverRate;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
