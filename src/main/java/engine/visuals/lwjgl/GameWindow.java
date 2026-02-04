package engine.visuals.lwjgl;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFWErrorCallback.createPrint;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS;
import static org.lwjgl.opengl.GLUtil.setupDebugMessageCallback;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import engine.common.math.Vector2i;
import engine.context.GameContextWrapper;
import engine.nengen.EngineConfiguration;
import engine.visuals.lwjgl.callback.CharCallback;
import engine.visuals.lwjgl.callback.KeyCallback;
import engine.visuals.lwjgl.callback.MouseButtonCallback;
import engine.visuals.lwjgl.callback.MouseMovementCallback;
import engine.visuals.lwjgl.callback.MouseScrollCallback;
import engine.visuals.lwjgl.callback.WindowResizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.Callback;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;

/**
 * @author Lunkle
 */
public class GameWindow {

	/**
	 * Whether to set a debug message callback. Turn this off for better performance.
	 */
	private static final boolean DEBUG = true;
	private Callback debugMessageCallback;

	private long windowId = NULL;
	private final String windowTitle;
	private final boolean fullScreen;
	private boolean resizable = true;
	private Vector2i windowDimensions;
	private long sharedContextWindowHandle;

	private final GLContext glContext;

	private final GameContextWrapper wrapper;

	public GameWindow(EngineConfiguration configuration, GameContextWrapper wrapper, GLContext glContext) {
		this.windowTitle = configuration.windowTitle();
		this.resizable = configuration.resizable();
		this.fullScreen = configuration.fullscreen();
		this.windowDimensions = configuration.windowDim();
		this.wrapper = requireNonNull(wrapper);
		this.glContext = requireNonNull(glContext);
	}

	public void createDisplay() {
		glfwSetErrorCallback(createPrint(System.err).set());
		Configuration.GLFW_CHECK_THREAD0.set(false);
		boolean initSuccess = glfwInit();
		assert initSuccess : "GLFW initialization failed";
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3); // Use GLFW version 3.3
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_SAMPLES, 4);
		if (DEBUG) {
			glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GL_TRUE);
		}
		long primaryMonitor = glfwGetPrimaryMonitor();
		GLFWVidMode vidmode = glfwGetVideoMode(primaryMonitor); // Get the resolution of the primary monitor
		assert vidmode != null : "Failed to get video mode for primary monitor";
		if (fullScreen)
			windowDimensions = new Vector2i(vidmode.width(), vidmode.height());
		windowId = glfwCreateWindow(windowDimensions.x(), windowDimensions.y(), windowTitle, fullScreen ? primaryMonitor : NULL, NULL); // Create the window
		glContext.setWindowDim(windowDimensions);
		assert windowId != NULL : "Failed to create the GLFW window";
		glfwSetWindowPos(windowId, (vidmode.width() - windowDimensions.x()) / 2, (vidmode.height() - windowDimensions.y()) / 2); // Center the window
		glfwMakeContextCurrent(windowId); // Make the OpenGL context current
		createCapabilities();
		glEnable(GL_BLEND);
		glEnable(GL_MULTISAMPLE);
		if (DEBUG) {
			glEnable(GL_DEBUG_OUTPUT);
			glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
		}
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glfwSwapInterval(1); // Enable v-sync
		glfwShowWindow(windowId); // Make the window visible
		// Use the framebuffer size for the viewport, which may be different from the
		// window size on high-DPI displays.
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetFramebufferSize(windowId, pWidth, pHeight);
			glViewport(0, 0, pWidth.get(0), pHeight.get(0));
		}
	}

	public void createSharedContextWindow() {
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		sharedContextWindowHandle = glfwCreateWindow(1, 1, "", NULL, windowId);
		assert sharedContextWindowHandle != NULL : "Failed to create shared context window";
	}

	public void attachCallbacks() {
		if (DEBUG) {
			debugMessageCallback = setupDebugMessageCallback();
		}
		glfwSetKeyCallback(windowId, new KeyCallback(wrapper));
		glfwSetCharCallback(windowId, new CharCallback(wrapper));
		glfwSetMouseButtonCallback(windowId, new MouseButtonCallback(wrapper));
		glfwSetScrollCallback(windowId, new MouseScrollCallback(wrapper));
		glfwSetCursorPosCallback(windowId, new MouseMovementCallback(wrapper));
		glfwSetFramebufferSizeCallback(windowId, new WindowResizeCallback(wrapper, glContext));
	}

	public void destroy() {
		if (DEBUG && debugMessageCallback != null) {
			debugMessageCallback.close();
		}
		if (sharedContextWindowHandle != NULL) {
			glfwDestroyWindow(sharedContextWindowHandle);
		}
		if (windowId != NULL) {
			glfwFreeCallbacks(windowId); // Release callbacks
			glfwDestroyWindow(windowId); // Release window
		}
		glfwTerminate(); // Terminate GLFW
		Callback errorCallback = glfwSetErrorCallback(null);
		if (errorCallback != null) {
			errorCallback.free(); // Release the error callback
		}
	}

	public long windowId() {
		return windowId;
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(windowId);
	}

	public long getSharedContextWindowHandle() {
		return sharedContextWindowHandle;
	}

}
