package nomadrealms.app.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StudioSplashContextTest {

	@Test
	public void testFrameCounterAndAutoTransition() {
		StudioSplashContext splashContext = new StudioSplashContext();
		assertEquals(0, splashContext.frameCounter());

		splashContext.update();
		assertEquals(1, splashContext.frameCounter());

		for (int i = 0; i < 59; i++) {
			splashContext.update();
		}
		assertEquals(60, splashContext.frameCounter());
		assertTrue(splashContext.transitionStarted());
	}

}
