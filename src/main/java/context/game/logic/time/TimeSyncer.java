package context.game.logic.time;

import static app.NomadRealmsClient.TICK_TIME;
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
			return TICK_TIME;
		}
		int n;
		float tickTimeChangeRate;
		if (timeOffset > 0) {
			n = (int) ceil(timeOffset / (MINIMUM_TICK_TIME - TICK_TIME));
			tickTimeChangeRate = (MINIMUM_TICK_TIME - TICK_TIME) * (MINIMUM_TICK_TIME - TICK_TIME) / timeOffset;
		} else {
			n = (int) ceil(sqrt(timeOffset * 1.0 / MAX_TICK_TIME_CHANGE_RATE));
			tickTimeChangeRate = timeOffset * 1f / (n * n);
		}

		float newTickTime = TICK_TIME + (n - abs(++ticksElapsed - n)) * tickTimeChangeRate;
		if (abs(timeOffsetProgress + TICK_TIME - newTickTime) > abs(timeOffset)) {
			newTickTime = timeOffsetProgress - timeOffset + TICK_TIME;
		}
		timeOffsetProgress += TICK_TIME - newTickTime;
		return newTickTime;
	}

	float calculateNewTickRate() {
		return 1 / calculateNewTickTime();
	}

	public float timeOffsetProgress() {
		return timeOffsetProgress;
	}

}
