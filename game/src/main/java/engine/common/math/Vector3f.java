package engine.common.math;

import java.util.Objects;

/**
 * An immutable vector of three floats.
 *
 * @author Jay
 */
public class Vector3f {

	public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);

	float x;
	float y;
	float z;

	public Vector3f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(Vector3f src) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
	}

	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
	}

	public Vector3f add(Vector3f vector) {
		return new Vector3f(x + vector.x, y + vector.y, z + vector.z);
	}

	public Vector3f sub(Vector3f vector) {
		return new Vector3f(x - vector.x, y - vector.y, z - vector.z);
	}

	public Vector3f scale(float scale) {
		return new Vector3f(x * scale, y * scale, z * scale);
	}

	public Vector3f normalise() {
		float len = length();
		if (Math.abs(len) > 0.0001f) {
			return scale(1f / len);
		} else {
			return new Vector3f();
		}
	}

	public float lengthSquared() {
		return x * x + y * y + z * z;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public float dot(Vector3f vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	public Vector3f cross(Vector3f vector) {
		return new Vector3f(y * vector.z - z * vector.y, vector.x * z - vector.z * x, x * vector.y - y * vector.x);
	}

	public float angle(Vector3f vector) {
		float dls = dot(vector) / (length() * vector.length());
		if (dls < -1f)
			dls = -1f;
		else if (dls > 1.0f)
			dls = 1.0f;
		return (float) Math.toDegrees(Math.acos(dls));
	}

	public Vector3f projectOnto(Vector3f vector) {
		return vector.scale(dot(vector) / vector.lengthSquared());
	}

	public Vector3f reflect(Vector3f vector) {
		return projectOnto(vector).scale(2).sub(this);
	}

	public float x() {
		return x;
	}

	public float y() {
		return y;
	}

	public float z() {
		return z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "Vector3f[" + x + ", " + y + ", " + z + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector3f other = (Vector3f) obj;
		return x == other.x && y == other.y && z == other.z;
	}

}
