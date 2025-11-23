package engine.nengen;

import engine.common.math.Vector2i;

/**
 * External-facing configuration class for Nengen.
 * <br>
 * <br>
 * This write-only class is used to configure the Nengen engine before starting it. Once the engine is started, the
 * configuration is converted to an internal configuration class, which is used to run the engine.
 * <br>
 * <br>
 * The main purpose of the separation between the external-facing configuration class and the internal-facing
 * configuration class is to prevent the user from manually changing the configuration of the engine while it is
 * running.
 */
public class NengenConfiguration {

	protected int width = 800;
	protected int height = 600;
	protected int frameRate = 30;
	protected int tickRate = 10;
	protected boolean resizable = false;
	protected boolean fullscreen = false;
	protected String windowName;

	protected static boolean DEBUG = true;
	protected static boolean AUTO_GL = false;

	protected NengenConfiguration() {
	}

	public NengenConfiguration setWindowDim(Vector2i dim) {
		return setWindowDim(dim.x(), dim.y());
	}

	public NengenConfiguration setWindowDim(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public NengenConfiguration setWindowName(String windowName) {
		this.windowName = windowName;
		return this;
	}

	public NengenConfiguration setFrameRate(int frameRate) {
		assert frameRate > 0 : "Frame rate must be greater than 0";
		this.frameRate = frameRate;
		return this;
	}

	public NengenConfiguration setTickRate(int tickRate) {
		assert tickRate > 0 : "Tick rate must be greater than 0";
		this.tickRate = tickRate;
		return this;
	}

	public NengenConfiguration setResizable(boolean resizable) {
		this.resizable = resizable;
		return this;
	}

	public NengenConfiguration setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
		return this;
	}

	public NengenConfiguration debug() {
		DEBUG = true;
		return this;
	}

	public NengenConfiguration autoGL() {
		AUTO_GL = true;
		return this;
	}

	protected EngineConfiguration build() {
		return new EngineConfiguration(this);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getFrameRate() {
		return frameRate;
	}

	public int getTickRate() {
		return tickRate;
	}

	public int getMillisPerTick() {
		return 1000 / tickRate;
	}

	public boolean isResizable() {
		return resizable;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public String getWindowName() {
		return windowName;
	}

	public String toString() {
		return "NengenConfiguration [width=" + width + ", height=" + height + ", frameRate=" + frameRate + ", tickRate=" + tickRate + ", resizable=" + resizable + ", fullscreen=" + fullscreen + ", windowName=" + windowName + "]";
	}

}
