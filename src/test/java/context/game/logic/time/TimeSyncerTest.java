package context.game.logic.time;

import org.junit.Assert;
import org.junit.Test;

public class TimeSyncerTest {

	@Test
	public void testCalculateNewTickTime1() {
		TimeSyncer syncer = new TimeSyncer();
		int timeOffset = 539;
		calculateTickTimes(syncer, 539);
		Assert.assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);

		syncer.restartAndSetTimeOffset(1234);
		Assert.assertEquals(0, syncer.timeOffsetProgress(), 0);

		timeOffset = 1000;
		calculateTickTimes(syncer, timeOffset);
		Assert.assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);
	}

	@Test
	public void testCalculateNewTickTime2() {
		TimeSyncer syncer = new TimeSyncer();
		int timeOffset = -539;
		calculateTickTimes(syncer, timeOffset);
		Assert.assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);
	}

	@Test
	public void testCalculateNewTickTime3() {
		TimeSyncer syncer = new TimeSyncer();
		int timeOffset = -1;
		calculateTickTimes(syncer, timeOffset);
		Assert.assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);

		timeOffset = 1;
		calculateTickTimes(syncer, timeOffset);
		Assert.assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);
	}

	@Test
	public void testCalculateNewTickTime4() {
		TimeSyncer syncer = new TimeSyncer();
		int timeOffset = 0;
		int numCalculations = calculateTickTimes(syncer, timeOffset);
		Assert.assertEquals(1, numCalculations);
		Assert.assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);
	}

	private int calculateTickTimes(TimeSyncer syncer, int timeOffset) {
		syncer.restartAndSetTimeOffset(timeOffset);
		float frameTime = -1;
		int numCalculations = 0;
		while (frameTime != 100 && numCalculations < 1000) {
			frameTime = syncer.calculateNewTickTime();
			numCalculations++;
		}
		return numCalculations;
	}

}