package engine.common.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class PerformanceProfilerTest {

	@Test
	public void testProfiling() throws InterruptedException {
		PerformanceProfiler profiler = new PerformanceProfiler(2);

		profiler.startPhase("Phase1");
		long t1 = System.nanoTime();
		while(System.nanoTime() - t1 < 10_000_000);
		profiler.endPhase("Phase1");

		profiler.startPhase("Phase2");
		long t2 = System.nanoTime();
		while(System.nanoTime() - t2 < 20_000_000);
		profiler.endPhase("Phase2");

		profiler.nextFrame();

		Map<String, Float> averages = profiler.getAverageDurations();
		assertTrue(averages.get("Phase1") >= 0.009f, "Phase1 was " + averages.get("Phase1"));
		assertTrue(averages.get("Phase2") >= 0.019f, "Phase2 was " + averages.get("Phase2"));

		profiler.startPhase("Phase1");
		long t3 = System.nanoTime();
		while(System.nanoTime() - t3 < 10_000_000); // 10ms
		profiler.endPhase("Phase1");

		profiler.startPhase("Phase1");
		long t4 = System.nanoTime();
		while(System.nanoTime() - t4 < 20_000_000); // 20ms
		profiler.endPhase("Phase1");

		// Phase2 is NOT updated this frame.

		profiler.nextFrame();

		averages = profiler.getAverageDurations();
		// Phase 1 average should be (Phase1_frame0 + Phase1_frame1) / 2
		// Phase1_frame0 = ~10ms. Phase1_frame1 = ~30ms. Average = ~20ms.
		assertTrue(averages.get("Phase1") >= 0.014f, "Phase1 average was " + averages.get("Phase1"));
		// Phase 2 average should be (Phase2_frame0 + Phase2_frame1) / 2
		// Phase2_frame0 = ~20ms. Phase2_frame1 = 0ms. Average = ~10ms.
		assertTrue(averages.get("Phase2") > 0.009f, "Phase2 average was " + averages.get("Phase2"));
	}
}
