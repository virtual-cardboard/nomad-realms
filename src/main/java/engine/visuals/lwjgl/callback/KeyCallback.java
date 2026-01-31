package engine.visuals.lwjgl.callback;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

import engine.context.GameContext;
import engine.context.GameContextWrapper;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.KeyRepeatedInputEvent;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Decorates a GLFW key pressed event into a {@link KeyPressedInputEvent} and forwards it to the game context.
 * <br>
 * Decorates a GLFW key released event into a {@link KeyReleasedInputEvent} and forwards it to the game context.
 * <br>
 * Decorates a GLFW key repeated event into a {@link KeyRepeatedInputEvent} and forwards it to the game context.
 *
 * @author Lunkle
 */
public class KeyCallback extends GLFWKeyCallback {

	private final GameContextWrapper wrapper;

	public KeyCallback(GameContextWrapper wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * This is the function that is called internally by GLFW called when a key is pressed, released, or repeated.
	 */
	@Override
	public final void invoke(long window, int key, int scancode, int action, int mods) {
		if (key != GLFW_KEY_UNKNOWN) {
			GameContext context = wrapper.context();
			if (!context.initialized()) {
				return;
			}
			switch (action) {
				case GLFW_PRESS:
					context.input(new KeyPressedInputEvent(key));
					break;
				case GLFW_RELEASE:
					context.input(new KeyReleasedInputEvent(key));
					break;
				case GLFW_REPEAT:
					context.input(new KeyRepeatedInputEvent(key));
					break;
				default:
					break;
			}
		}
	}

}
