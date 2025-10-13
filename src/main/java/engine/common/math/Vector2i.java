package engine.common.math;

import java.util.Objects;

/**
 * An immutable vector of two ints.
 *
 * @author Jay
 */

public class Vector2i  {

	int x;
	int y;

	public Vector2i() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2i(Vector2i src) {
		this.x = src.x;
		this.y = src.y;
	}

	public Vector2i negate() {
		return new Vector2i(-x, -y);
	}

	public Vector2i add(int x, int y) {
		return new Vector2i(this.x + x, this.y + y);
	}

	public Vector2i add(Vector2i vector) {
		return new Vector2i(x + vector.x, y + vector.y);
	}

	public Vector2i sub(Vector2i vector) {
		return new Vector2i(x - vector.x, y - vector.y);
	}

	public Vector2i scale(float scale) {
		return new Vector2i((int) (x * scale), (int) (y * scale));
	}

	public Vector2i scale(int scale) {
		return new Vector2i(x * scale, y * scale);
	}

	public Vector2i multiply(int x, int y) {
		return new Vector2i(this.x * x, this.y * y);
	}

	public Vector2i normalise() {
		float len = length();
		if (len != 0f) {
			return scale(1f / len);
		} else
			throw new IllegalStateException("Zero length vector");
	}

	public int lengthSquared() {
		return x * x + y * y;
	}

	public int length() {
		return (int) Math.sqrt(x * x + y * y);
	}

	public int dot(Vector2i vector) {
		return x * vector.x + y * vector.y;
	}

	public float angle() {
		return (float) ((Math.atan2(y, x) + 2 * Math.PI) % (2 * Math.PI));
	}

	public Vector2f toVec2f() {
		return new Vector2f(x, y);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "Vector2i[" + x + ", " + y + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector2i other = (Vector2i) obj;
		return x == other.x && y == other.y;
	}

}
