package engine.common.math;

import java.util.Objects;

public class Vector3l {

	long x;
	long y;
	long z;

	public Vector3l() {
	}

	public Vector3l(long x, long y, long z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3l(Vector3l src) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
	}

	public Vector3l negate() {
		return new Vector3l(-x, -y, -z);
	}

	public Vector3l add(Vector3l vector) {
		return new Vector3l(x + vector.x, y + vector.y, z + vector.z);
	}

	public Vector3l sub(Vector3l vector) {
		return new Vector3l(x - vector.x, y - vector.y, z - vector.z);
	}

	public Vector3l scale(float scale) {
		return new Vector3l((long) (x * scale), (long) (y * scale), (long) (z * scale));
	}

	public Vector3l normalise() {
		float len = length();
		if (len != 0f) {
			return scale(1f / len);
		} else {
			throw new IllegalStateException("Zero length vector");
		}
	}

	public long lengthSquared() {
		return x * x + y * y + z * z;
	}

	public long length() {
		return (long) Math.sqrt(lengthSquared());
	}

	public float dot(Vector3l vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	public long x() {
		return x;
	}

	public long y() {
		return y;
	}

	public long z() {
		return z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public String toString() {
		return "Vector3l[" + x + ", " + y + ", " + z + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector3l other = (Vector3l) obj;
		return x == other.x && y == other.y && z == other.z;
	}

}
