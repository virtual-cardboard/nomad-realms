package engine.common.math;

import java.util.Objects;

/**
 * An immutable vector of two longs.
 *
 * @author Jay
 */
public class Vector2l {

	long x;
	long y;

	public Vector2l() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2l(long x, long y) {
		this.x = x;
		this.y = y;
	}

	public Vector2l(Vector2l src) {
		this.x = src.x;
		this.y = src.y;
	}

	public Vector2l negate() {
		return new Vector2l(-x, -y);
	}

	public Vector2l add(Vector2l vector) {
		return new Vector2l(x + vector.x, y + vector.y);
	}

	public Vector2l sub(Vector2l vector) {
		return new Vector2l(x - vector.x, y - vector.y);
	}

	public Vector2l scale(float scale) {
		return new Vector2l((long) (x * scale), (long) (y * scale));
	}

	public Vector2l normalise() {
		float len = length();
		if (len != 0f) {
			return scale(1f / len);
		} else
			throw new IllegalStateException("Zero length vector");
	}

	public long lengthSquared() {
		return x * x + y * y;
	}

	public long length() {
		return (long) Math.sqrt(x * x + y * y);
	}

	public float dot(Vector2l vector) {
		return x * vector.x + y * vector.y;
	}

	public float angle() {
		return (float) ((Math.atan2(y, x) + 2 * Math.PI) % (2 * Math.PI));
	}

	public long x() {
		return x;
	}

	public long y() {
		return y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "Vector2l[" + x + ", " + y + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector2l other = (Vector2l) obj;
		return x == other.x && y == other.y;
	}

}
