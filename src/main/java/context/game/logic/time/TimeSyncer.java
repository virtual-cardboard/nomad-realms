package context.game.logic.time;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public class TimeSyncer {

	private static final float MINIMUM_TICK_TIME = 25;
	public static final int MAX_TICK_TIME_CHANGE_RATE = 25;

	private int timeOffset;
	private float timeOffsetProgress;
	private int ticksElapsed;

	void restartAndSetTimeOffset(int timeOffset) {
		this.timeOffset = timeOffset;
		ticksElapsed = 0;
		timeOffsetProgress = 0;
	}

	float calculateNewTickTime() {
		if (abs(timeOffsetProgress - timeOffset) < 0.1f) {
			return 100;
		}
		int n;
		float tickTimeChangeRate;
		if (timeOffset < 0) {
			n = (int) ceil(timeOffset / (MINIMUM_TICK_TIME - 100));
			tickTimeChangeRate = (MINIMUM_TICK_TIME - 100) * (MINIMUM_TICK_TIME - 100) / timeOffset;
		} else {
			n = (int) ceil(sqrt(timeOffset * 1.0 / MAX_TICK_TIME_CHANGE_RATE));
			tickTimeChangeRate = timeOffset * 1f / (n * n);
		}

		float newTickTime = 100 + (n - abs(++ticksElapsed - n)) * tickTimeChangeRate;
		if (abs(timeOffsetProgress + newTickTime - 100) > abs(timeOffset)) {
			newTickTime = timeOffset - timeOffsetProgress + 100;
		}
		timeOffsetProgress += newTickTime - 100;
		return newTickTime;
	}

	float calculateNewTickRate() {
		return 1 / calculateNewTickTime();
	}

	public float timeOffsetProgress() {
		return timeOffsetProgress;
	}

	public static void main(String[] args) {
		TimeSyncer syncer = new TimeSyncer();
		int timeOffset = 539;
		syncer.restartAndSetTimeOffset(timeOffset);
		for (int i = 0; i < 50; i++) {
			float newFrameTime = syncer.calculateNewTickTime();
			System.out.println(newFrameTime + " " + syncer.timeOffsetProgress());
		}
	}

}
