package context.game.logic.time;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TimeSyncerTest {

	@Test
	void testCalculateNewTickTime1() {
		TimeSyncer syncer = new TimeSyncer();
		int timeOffset = 539;
		calculateTickTimes(syncer, 539);
		assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);

		syncer.restartAndSetTimeOffset(1234);
		assertEquals(0, syncer.timeOffsetProgress(), 0);

		timeOffset = 1000;
		calculateTickTimes(syncer, timeOffset);
		assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);
	}

	@Test
	void testCalculateNewTickTime2() {
		TimeSyncer syncer = new TimeSyncer();
		int timeOffset = -539;
		calculateTickTimes(syncer, timeOffset);
		assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);
	}

	@Test
	void testCalculateNewTickTime3() {
		TimeSyncer syncer = new TimeSyncer();
		int timeOffset = -1;
		calculateTickTimes(syncer, timeOffset);
		assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);

		timeOffset = 1;
		calculateTickTimes(syncer, timeOffset);
		assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);
	}

	@Test
	void testCalculateNewTickTime4() {
		TimeSyncer syncer = new TimeSyncer();
		int timeOffset = 0;
		int numCalculations = calculateTickTimes(syncer, timeOffset);
		assertEquals(1, numCalculations);
		assertEquals(timeOffset, syncer.timeOffsetProgress(), 0.1f);
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
