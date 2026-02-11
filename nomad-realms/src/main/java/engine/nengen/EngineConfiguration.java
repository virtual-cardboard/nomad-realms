package engine.nengen;

import engine.common.math.Vector2i;
import engine.context.GameWindowUpdater;

/**
 * Internal configuration class for Nengen. This class is used to run the engine.
 *
 * @author Lunkle
 */
public class EngineConfiguration {

	protected int width = 800;
	protected int height = 600;
	protected int frameRate = 30;
	protected int tickRate = 10;
	protected boolean resizable = false;
	protected boolean fullscreen = false;
	protected String windowTitle;

	protected boolean shouldClose = false;

	public static boolean DEBUG = false;
	public static boolean AUTO_GL = true;

	protected EngineConfiguration(NengenConfiguration configuration) {
		this.width = configuration.width;
		this.height = configuration.height;
		this.frameRate = configuration.frameRate;
		this.tickRate = configuration.tickRate;
		this.resizable = configuration.resizable;
		this.fullscreen = configuration.fullscreen;
		this.windowTitle = configuration.windowName;
		DEBUG = NengenConfiguration.DEBUG;
		AUTO_GL = NengenConfiguration.AUTO_GL;
	}

	public EngineConfiguration setWindowDim(Vector2i dim) {
		return setWindowDim(dim.x(), dim.y());
	}

	public EngineConfiguration setWindowDim(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public EngineConfiguration setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
		return this;
	}

	public EngineConfiguration setFrameRate(int frameRate) {
		this.frameRate = frameRate;
		return this;
	}

	public EngineConfiguration setTickRate(int tickRate) {
		this.tickRate = tickRate;
		return this;
	}

	public EngineConfiguration setResizable(boolean resizable) {
		this.resizable = resizable;
		return this;
	}

	public EngineConfiguration setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
		return this;
	}

	/**
	 * Signals to the engine that it should close.
	 * <br>
	 * Currently, this is called by the {@link GameWindowUpdater} when the GLFW window is closed. The
	 * {@link GameWindowUpdater} sees the flag and closes the tick thread.
	 */
	public void setShouldClose() {
		this.shouldClose = true;
	}

	public static void DEBUG(String... message) {
		if (DEBUG) {
			StringBuilder sb = new StringBuilder();
			for (String msg : message) {
				sb.append(msg).append(" ");
			}
			System.out.println(sb);
		}
	}

	public int frameRate() {
		return frameRate;
	}

	public int tickRate() {
		return tickRate;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public boolean resizable() {
		return resizable;
	}

	public boolean fullscreen() {
		return fullscreen;
	}

	public String windowTitle() {
		return windowTitle;
	}

	public Vector2i windowDim() {
		return new Vector2i(width, height);
	}

	public boolean shouldClose() {
		return shouldClose;
	}

}
