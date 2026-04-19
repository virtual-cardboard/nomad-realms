package engine.nengen;

import engine.common.math.Matrix4f;

/**
 * A DrawBatch collects instance data (transforms and colors) and renders them in a single instanced draw call.
 * This interface provides a backend-agnostic way for the game logic to perform batch rendering.
 */
public interface DrawBatch {

	/**
	 * Adds an instance to the batch.
	 *
	 * @param transform the transformation matrix for the instance
	 * @param color     the color for the instance
	 */
	void add(Matrix4f transform, int color);

	/**
	 * Clears all instances from the batch. Should be called before each frame's data collection.
	 */
	void clear();

	/**
	 * Renders all instances currently in the batch.
	 */
	void draw();

}
