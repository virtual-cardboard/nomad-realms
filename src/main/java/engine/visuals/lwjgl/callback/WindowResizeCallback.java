package engine.visuals.lwjgl.callback;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.glViewport;

import java.nio.IntBuffer;

import engine.common.math.Vector2i;
import engine.context.GameContextWrapper;
import engine.context.input.event.FrameResizedInputEvent;
import engine.visuals.lwjgl.GLContext;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.system.MemoryStack;

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
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			glfwGetWindowSize(windowId, w, h);
			int windowWidth = w.get(0);
			int windowHeight = h.get(0);
			glContext.setWindowDim(new Vector2i(windowWidth, windowHeight));
			wrapper.context().input(new FrameResizedInputEvent(windowWidth, windowHeight));
		}
	}

}
