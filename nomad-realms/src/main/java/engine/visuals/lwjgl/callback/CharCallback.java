package engine.visuals.lwjgl.callback;

import engine.context.GameContext;
import engine.context.GameContextWrapper;
import engine.context.input.event.CharacterTypedInputEvent;
import org.lwjgl.glfw.GLFWCharCallback;

/**
 * Decorates a GLFW character typed event into a {@link CharacterTypedInputEvent} and forwards it to the game context.
 *
 * @author Lunkle
 */
public class CharCallback extends GLFWCharCallback {

	private final GameContextWrapper wrapper;

	public CharCallback(GameContextWrapper wrapper) {
		this.wrapper = wrapper;
	}

	@Override
	public void invoke(long window, int codepoint) {
		GameContext context = wrapper.context();
		if (context.initialized()) {
			context.input(new CharacterTypedInputEvent(codepoint));
		}
	}

}
