package engine.common.math;

import java.util.Objects;

/**
 * <p>
 * An immutable vector of two floats.
 * </p>
 * This is immutable so that a programmer can pass a <code>Vector2f</code> into a function without needing to worry
 * about being mutated.
 *
 * @author Jay
 */
public class Vector2f  {

	public static final Vector2f ORIGIN = new Vector2f(0, 0);

	float x;
	float y;

	public static Vector2f fromAngleLength(float angle, float length) {
		return new Vector2f((float) (length * Math.cos(angle)), (float) (length * Math.sin(angle)));
	}

	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f src) {
		this.x = src.x();
		this.y = src.y();
	}

	public Vector2f negate() {
		return new Vector2f(-x(), -y());
	}

	public Vector2f add(Vector2i vector) {
		return new Vector2f(x() + vector.x, y() + vector.y);
	}

	public Vector2f add(float x, float y) {
		return new Vector2f(this.x() + x, this.y() + y);
	}

	public Vector2f add(Vector2f vector) {
		return new Vector2f(x() + vector.x(), y() + vector.y());
	}

	public Vector2f sub(Vector2i vector) {
		return new Vector2f(x() - vector.x, y() - vector.y);
	}

	public Vector2f sub(float x, float y) {
		return new Vector2f(this.x() - x, this.y() - y);
	}

	public Vector2f sub(Vector2f vector) {
		return new Vector2f(x() - vector.x(), y() - vector.y());
	}

	public Vector2f multiply(float x, float y) {
		return new Vector2f(this.x() * x, this.y() * y);
	}

	public Vector2f multiply(Vector2f vector) {
		return new Vector2f(x() * vector.x(), y() * vector.y());
	}

	public Vector2f scale(float scale) {
		return new Vector2f(x() * scale, y() * scale);
	}

	public Vector2f scale(float x, float y) {
		return new Vector2f(this.x() * x, this.y() * y);
	}

	public Vector2f scale(Vector2f vector) {
		return new Vector2f(x() * vector.x(), y() * vector.y());
	}

	public Vector2f normalise() {
		float len = length();
		if (len != 0f) {
			return scale(1f / len);
		} else {
			throw new IllegalStateException("Zero length vector");
		}
	}

	public float lengthSquared() {
		return x() * x() + y() * y();
	}

	public float length() {
		return (float) Math.sqrt(x() * x() + y() * y());
	}

	public float dot(Vector2f vector) {
		return x() * vector.x() + y() * vector.y();
	}

	public float angle() {
		return (float) ((Math.atan2(y(), x()) + 2 * Math.PI) % (2 * Math.PI));
	}

	public Vector2f projectOnto(Vector2f vector) {
		return vector.scale(dot(vector) / vector.lengthSquared());
	}

	public Vector2f reflect(Vector2f vector) {
		return projectOnto(vector).scale(2).sub(this);
	}

	/**
	 * Rotates the vector around the origin, counterclockwise.
	 *
	 * @param radians the angle to rotate the vector by, in radians.
	 * @return the rotated vector
	 */
	public Vector2f rotate(float radians) {
		double sin = Math.sin(radians);
		double cos = Math.cos(radians);
		return new Vector2f((float) (x() * cos - y() * sin), (float) (x() * sin + y() * cos));
	}

	public float x() {
		return x;
	}

	public float y() {
		return y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x(), y());
	}

	@Override
	public String toString() {
		return "Vector2f[" + x() + ", " + y() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector2f other = (Vector2f) obj;
		return x() == other.x() && y() == other.y();
	}

}
