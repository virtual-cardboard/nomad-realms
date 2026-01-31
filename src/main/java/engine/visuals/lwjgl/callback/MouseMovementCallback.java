package engine.visuals.lwjgl.callback;

import engine.context.GameContext;
import engine.context.GameContextWrapper;
import engine.context.input.event.MouseMovedInputEvent;
import org.lwjgl.glfw.GLFWCursorPosCallback;

/**
 * Decorates a GLFW mouse moved event into a {@link MouseMovedInputEvent} and forwards it to the game context.
 *
 * @author Lunkle
 */
public class MouseMovementCallback extends GLFWCursorPosCallback {

	private final GameContextWrapper wrapper;

	public MouseMovementCallback(GameContextWrapper wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * This is the function that is called internally by GLFW called when the mouse is moved.
	 */
	@Override
	public void invoke(long window, double xPos, double yPos) {
		int oldX = wrapper.mouse().x();
		int oldY = wrapper.mouse().y();
		wrapper.mouse().x((int) xPos);
		wrapper.mouse().y((int) yPos);
		GameContext context = wrapper.context();
		if (!context.initialized()) {
			return;
		}
		context.input(new MouseMovedInputEvent(wrapper.mouse(), (int) xPos, (int) yPos, oldX, oldY));
	}

}
