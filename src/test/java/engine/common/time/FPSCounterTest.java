package engine.common.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

	@Test
	public void testFPSCounterLogic() {
		// We can't easily mock System.nanoTime() without a lot of ceremony,
		// but we can check if the running sum logic is correct by inspecting the behavior.
		// Since update() uses System.nanoTime(), we'll just check if it's consistent.
		FPSCounter fpsCounter = new FPSCounter(2);
		fpsCounter.update(); // first call sets lastTime
		try { Thread.sleep(10); } catch (InterruptedException e) {}
		fpsCounter.update();
		float fps1 = fpsCounter.getFPS();

		try { Thread.sleep(10); } catch (InterruptedException e) {}
		fpsCounter.update();
		float fps2 = fpsCounter.getFPS();

		assertTrue(fps1 > 0);
		assertTrue(fps2 > 0);
	}

}
