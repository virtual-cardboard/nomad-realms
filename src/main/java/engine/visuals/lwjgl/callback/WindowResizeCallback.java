package engine.visuals.lwjgl.callback;

import static org.lwjgl.opengl.GL11.glViewport;

import engine.common.math.Vector2i;
import engine.context.GameContextWrapper;
import engine.context.input.event.FrameResizedInputEvent;
import engine.visuals.lwjgl.GLContext;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

/**
 * Decorates a GLFW window resize event into a {@link FrameResizedInputEvent} and forwards it to the game context.
 *
 * @author Lunkle
 */
public class WindowResizeCallback extends GLFWFramebufferSizeCallback {

	private final GameContextWrapper wrapper;
	private final GLContext glContext;

	public WindowResizeCallback(GameContextWrapper wrapper, GLContext glContext) {
		this.wrapper = wrapper;
		this.glContext = glContext;
	}

	/**
	 * This is the function that is called internally by GLFW called when the window is resized.
	 */
	@Override
	public void invoke(long windowId, int width, int height) {
		glViewport(0, 0, width, height);
		glContext.setWindowDim(new Vector2i(width, height));
		wrapper.context().input(new FrameResizedInputEvent(width, height));
	}

}
