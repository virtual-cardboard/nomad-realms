package nomadrealms.context.game.card.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import engine.common.math.Vector2f;

public class WalkActionRenderScaleTest {

	@Test
	public void testDefaultActionRenderScale() {
		Action defaultAction = new Action() {
			@Override
			public void update(nomadrealms.context.game.world.World world) {}
			@Override
			public boolean isComplete() { return true; }
			@Override
			public int preDelay() { return 0; }
			@Override
			public int postDelay() { return 0; }
		};
		Vector2f scale = defaultAction.renderScale(null);
		assertNotNull(scale);
		assertEquals(1.0f, scale.x(), 0.001f);
		assertEquals(1.0f, scale.y(), 0.001f);
	}
}
