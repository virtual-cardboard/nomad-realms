package nomadrealms.render.ui.custom.tooltip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

import engine.common.math.Vector2i;
import engine.context.input.Mouse;
import engine.context.input.event.InputCallbackRegistry;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ScreenContainerContent;
import org.junit.jupiter.api.Test;

public class TooltipTest {

	@Test
	public void testTooltipFollowsMouseAndClampsToScreen() throws Exception {
		GLContext glContext = new GLContext();
		glContext.setWindowDim(new Vector2i(1920, 1080));

		Mouse mouse = new Mouse();
		InputCallbackRegistry registry = new InputCallbackRegistry();
		ScreenContainerContent screenContainerContent = new ScreenContainerContent(glContext.screen);

		Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
		unsafeField.setAccessible(true);
		Unsafe unsafe = (Unsafe) unsafeField.get(null);

		RenderingEnvironment re = (RenderingEnvironment) unsafe.allocateInstance(RenderingEnvironment.class);
		re.glContext = glContext;

		Tooltip tooltip = new Tooltip(re, screenContainerContent, null, mouse, registry);

		mouse.x(100);
		mouse.y(150);

		assertEquals(100.0f, tooltip.uiContainer().constraintBox().x().get());
		assertEquals(150.0f, tooltip.uiContainer().constraintBox().y().get());

		mouse.x(500);
		mouse.y(600);

		assertEquals(500.0f, tooltip.uiContainer().constraintBox().x().get());
		assertEquals(600.0f, tooltip.uiContainer().constraintBox().y().get());

		// Test clamping on negative coordinates
		mouse.x(-50);
		mouse.y(-20);

		assertEquals(0.0f, tooltip.uiContainer().constraintBox().x().get());
		assertEquals(0.0f, tooltip.uiContainer().constraintBox().y().get());

		// Test clamping near right / bottom edge
		mouse.x(2000);
		mouse.y(1200);

		assertEquals(1920.0f, tooltip.uiContainer().constraintBox().x().get());
		assertEquals(1080.0f, tooltip.uiContainer().constraintBox().y().get());
	}

}
