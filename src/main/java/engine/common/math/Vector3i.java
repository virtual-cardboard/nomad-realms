package engine.common.math;

import java.util.Objects;

/**
 * An immutable vector of three ints.
 *
 * @author Jay
 */
public class Vector3i {

	int x;
	int y;
	int z;

	public Vector3i() {
	}

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3i(Vector3i src) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
	}

	/**
	 * Negate the vector
	 *
	 * @return the negated vector
	 */
	public Vector3i negate() {
		return new Vector3i(-x, -y, -z);
	}

	public Vector3i add(int x, int y, int z) {
		return new Vector3i(this.x + x, this.y + y, this.z + z);
	}

	public Vector3i add(Vector3i vector) {
		return new Vector3i(x + vector.x, y + vector.y, z + vector.z);
	}

	public Vector3i sub(Vector3i vector) {
		return new Vector3i(x - vector.x, y - vector.y, z - vector.z);
	}

	public Vector3i scale(float scale) {
		return new Vector3i((int) (x * scale), (int) (y * scale), (int) (z * scale));
	}

	public Vector3i normalise() {
		float length = length();
		return new Vector3i((int) (x / length), (int) (y / length), (int) (z / length));
	}

	public int lengthSquared() {
		return x * x + y * y + z * z;
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public int dot(Vector3i vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	public Vector3f toVec3f() {
		return new Vector3f(x, y, z);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public String toString() {
		return "Vector3i[" + x + ", " + y + "," + z + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector3i other = (Vector3i) obj;
		return x == other.x && y == other.y && z == other.z;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int z() {
		return z;
	}

}
