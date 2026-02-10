package engine.common.time;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FPSCounterTest {

	@Test
	public void testFPSCounter() throws InterruptedException {
		FPSCounter fpsCounter = new FPSCounter(10);
		for (int i = 0; i < 20; i++) {
			fpsCounter.update();
			Thread.sleep(10);
		}
		float fps = fpsCounter.getFPS();
		assertTrue(fps > 0, "FPS should be greater than 0");
		// 10ms sleep might be slightly more or less depending on the system, but 1000 is a safe upper bound
		assertTrue(fps <= 1000, "FPS should be reasonable, but got " + fps);
	}

}
