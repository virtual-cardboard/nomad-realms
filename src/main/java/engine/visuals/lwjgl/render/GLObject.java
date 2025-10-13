package engine.visuals.lwjgl.render;

import engine.visuals.lwjgl.ResourcePack;

/**
 * @author Lunkle
 */
public abstract class GLObject {

	protected int id;
	private boolean initialized;

	public void genID(){}

	protected final void initialize() {
		if (initialized) {
			throw new IllegalStateException("GLObject already initialized.");
		}
		initialized = true;
	}

	protected final void verifyInitialized() {
		if (!initialized) {
			throw new IllegalStateException("GLObject not initialized.");
		}
	}

	protected void verifyNotInitialized() {
		if (initialized) {
			throw new IllegalStateException("GLObject already initialized.");
		}
	}

	public void putInto(String name, ResourcePack resourcePack) {
	}

}
