package context.game.logic.time;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public class TimeSyncer {

	private static final float MINIMUM_TICK_TIME = 25;
	private static final float MAX_TICK_TIME_CHANGE_RATE = 25;

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
		if (timeOffset > 0) {
			n = (int) ceil(timeOffset / (MINIMUM_TICK_TIME - 100));
			tickTimeChangeRate = (MINIMUM_TICK_TIME - 100) * (MINIMUM_TICK_TIME - 100) / timeOffset;
		} else {
			n = (int) ceil(sqrt(timeOffset * 1.0 / MAX_TICK_TIME_CHANGE_RATE));
			tickTimeChangeRate = timeOffset * 1f / (n * n);
		}

		float newTickTime = 100 + (n - abs(++ticksElapsed - n)) * tickTimeChangeRate;
		if (abs(timeOffsetProgress + 100 - newTickTime) > abs(timeOffset)) {
			newTickTime = timeOffsetProgress - timeOffset + 100;
		}
		timeOffsetProgress += 100 - newTickTime;
		return newTickTime;
	}

	float calculateNewTickRate() {
		return 1 / calculateNewTickTime();
	}

	public float timeOffsetProgress() {
		return timeOffsetProgress;
	}

}
