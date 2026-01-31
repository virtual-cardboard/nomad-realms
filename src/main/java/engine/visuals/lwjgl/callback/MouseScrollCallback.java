package engine.visuals.lwjgl.callback;

import engine.context.GameContext;
import engine.context.GameContextWrapper;
import engine.context.input.event.MouseScrolledInputEvent;
import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * Decorates a GLFW mouse scrolled event into a {@link MouseScrolledInputEvent} and forwards it to the game context.
 *
 * @author Lunkle
 */
public class MouseScrollCallback extends GLFWScrollCallback {

	private final GameContextWrapper wrapper;

	public MouseScrollCallback(GameContextWrapper wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * This is the function that is called internally by GLFW called when the mouse wheel is scrolled.
	 */
	@Override
	public void invoke(long window, double xOffset, double yOffset) {
		GameContext context = wrapper.context();
		if (!context.initialized()) {
			return;
		}
		context.input(new MouseScrolledInputEvent(wrapper.mouse(), (int) xOffset, (int) yOffset));
	}

}
