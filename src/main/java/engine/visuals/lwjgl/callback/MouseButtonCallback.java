package engine.visuals.lwjgl.callback;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

import engine.context.GameContext;
import engine.context.GameContextWrapper;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 * Decorates a GLFW mouse pressed event into a {@link MousePressedInputEvent} and forwards it to the game context.
 * <br>
 * Decorates a GLFW mouse released event into a {@link MouseReleasedInputEvent} and forwards it to the game context.
 *
 * @author Lunkle
 */
public class MouseButtonCallback extends GLFWMouseButtonCallback {

	private final GameContextWrapper wrapper;

	public MouseButtonCallback(GameContextWrapper wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * This is the function that is called internally by GLFW called when a mouse button is pressed, released, or
	 * repeated.
	 */
	@Override
	public void invoke(long window, int button, int action, int mods) {
		GameContext context = wrapper.context();
		if (!context.initialized()) {
			return;
		}
		switch (action) {
			case GLFW_PRESS:
				context.input(new MousePressedInputEvent(wrapper.mouse(), button));
				break;
			case GLFW_RELEASE:
				context.input(new MouseReleasedInputEvent(wrapper.mouse(), button));
				break;
			case GLFW_REPEAT:
				// We don't care about mouse repeats. Who even uses that?
			default:
				break;
		}
	}

}
