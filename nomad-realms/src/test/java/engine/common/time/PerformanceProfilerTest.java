package engine.common.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class PerformanceProfilerTest {

	@Test
	public void testProfiling() throws InterruptedException {
		PerformanceProfiler profiler = new PerformanceProfiler(2);

		profiler.profile("Phase1", () -> {
			long t1 = System.nanoTime();
			while (System.nanoTime() - t1 < 10_000_000) ;
		});

		profiler.profile("Phase2", () -> {
			long t2 = System.nanoTime();
			while (System.nanoTime() - t2 < 20_000_000) ;
		});

		profiler.nextFrame();

		Map<String, Float> averages = profiler.getAverageDurations();
		assertTrue(averages.get("Phase1") >= 0.009f, "Phase1 was " + averages.get("Phase1"));
		assertTrue(averages.get("Phase2") >= 0.019f, "Phase2 was " + averages.get("Phase2"));

		profiler.profile("Phase1", () -> {
			long t3 = System.nanoTime();
			while (System.nanoTime() - t3 < 10_000_000) ; // 10ms
		});

		profiler.profile("Phase1", () -> {
			long t4 = System.nanoTime();
			while (System.nanoTime() - t4 < 20_000_000) ; // 20ms
		});

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

	@Test
	public void testHierarchicalProfiling() {
		PerformanceProfiler profiler = new PerformanceProfiler(1);

		profiler.profile("Root", () -> {
			long t1 = System.nanoTime();
			while (System.nanoTime() - t1 < 5_000_000);

			profiler.profile("Child1", () -> {
				long t2 = System.nanoTime();
				while (System.nanoTime() - t2 < 5_000_000);
			});

			profiler.profile("Child2", () -> {
				long t3 = System.nanoTime();
				while (System.nanoTime() - t3 < 5_000_000);
			});
		});

		profiler.nextFrame();

		List<PerformanceProfiler.ProfileNode> roots = profiler.getRootNodes();
		assertEquals(1, roots.size());
		PerformanceProfiler.ProfileNode root = roots.get(0);
		assertEquals("Root", root.name());
		assertTrue(root.averageDuration() >= 0.014f, "Root duration was " + root.averageDuration());

		assertEquals(2, root.children().size());
		PerformanceProfiler.ProfileNode child1 = root.children().get("Child1");
		assertNotNull(child1);
		assertEquals("Child1", child1.name());
		assertTrue(child1.averageDuration() >= 0.004f);

		PerformanceProfiler.ProfileNode child2 = root.children().get("Child2");
		assertNotNull(child2);
		assertEquals("Child2", child2.name());
		assertTrue(child2.averageDuration() >= 0.004f);
	}

	@Test
	public void testNoNodeProliferationOverMultipleFrames() {
		PerformanceProfiler profiler = new PerformanceProfiler(100);

		for (int frame = 0; frame < 50; frame++) {
			profiler.profile("Render Total", () -> {
				profiler.profile("Render World", () -> {
					profiler.profile("Render Map", () -> {
						profiler.profile("Collect", () -> {});
						profiler.profile("Draw", () -> {});
						profiler.profile("Decorations", () -> {});
					});
					profiler.profile("Actors", () -> {});
					profiler.profile("Clouds", () -> {});
					profiler.profile("Particles", () -> {});
				});
				profiler.profile("Render Debug UI", () -> {});
				profiler.profile("Render Game UI", () -> {});
			});
			profiler.nextFrame();

			assertEquals(1, profiler.getRootNodes().size(), "Root count should stay 1");
			PerformanceProfiler.ProfileNode root = profiler.getRootNodes().get(0);
			assertEquals("Render Total", root.name());
			assertEquals(3, root.children().size(), "Render Total children count should stay 3");
			PerformanceProfiler.ProfileNode renderWorld = root.children().get("Render World");
			assertNotNull(renderWorld);
			assertEquals(4, renderWorld.children().size(), "Render World children count should stay 4");
			PerformanceProfiler.ProfileNode renderMap = renderWorld.children().get("Render Map");
			assertNotNull(renderMap);
			assertEquals(3, renderMap.children().size(), "Render Map children count should stay 3");
		}
	}
}
