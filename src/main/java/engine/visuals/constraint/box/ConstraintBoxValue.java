package engine.visuals.constraint.box;

import engine.common.math.Vector2f;

/**
 * {@link ConstraintBoxValue} objects are temporary and are used to store the values of a {@link ConstraintBox} at a
 * given time
 */
public class ConstraintBoxValue {

	private final float x;
	private final float y;
	private final float w;
	private final float h;

	/**
	 * Creates a new {@link ConstraintBoxValue} with the given values
	 *
	 * @param x the x value
	 * @param y the y value
	 * @param w the width value
	 * @param h the height value
	 */
	public ConstraintBoxValue(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * @return the x value
	 */
	public float x() {
		return x;
	}

	/**
	 * @return the y value
	 */
	public float y() {
		return y;
	}

	/**
	 * @return the width value
	 */
	public float w() {
		return w;
	}

	/**
	 * @return the height value
	 */
	public float h() {
		return h;
	}

	/**
	 * @return the position as a {@link Vector2f}
	 */
	public Vector2f pos() {
		return new Vector2f(x, y);
	}

	/**
	 * @return the dimensions as a {@link Vector2f}
	 */
	public Vector2f dim() {
		return new Vector2f(w, h);
	}

	@Override
	public String toString() {
		return "ConstraintBoxValue{" +
				"x=" + x +
				", y=" + y +
				", w=" + w +
				", h=" + h +
				'}';
	}

}
