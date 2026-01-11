package engine.common.math;

import java.util.Objects;

import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;

/**
 * An immutable vector of four floats.
 *
 * @author Jay
 */
public class Vector4f {

	float x, y, z, w;

	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4f() {
		this(0, 0, 0, 0);
	}


	public Vector4f(Vector4f src) {
		this(src.x, src.y, src.z, src.w);
	}

	public Vector4f(ConstraintPair c1, ConstraintPair c2) {
		this(c1.x().get(), c1.y().get(), c2.x().get(), c2.y().get());
	}

	public Vector4f(ConstraintBox box) {
		this(box.coordinate(), box.dimensions());
	}

	public Vector4f negate() {
		return new Vector4f(-x, -y, -z, -w);
	}

	public Vector4f add(Vector4f vector) {
		return new Vector4f(x + vector.x, y + vector.y, z + vector.z, w + vector.w);
	}

	public Vector4f sub(Vector4f vector) {
		return new Vector4f(x - vector.x, y - vector.y, z - vector.z, w - vector.w);
	}

	public Vector4f scale(float scale) {
		return new Vector4f(x * scale, y * scale, z * scale, w * scale);
	}

	public Vector4f normalise() {
		float len = length();
		if (len != 0f) {
			return scale(1f / len);
		} else
			throw new IllegalStateException("Zero length vector");
	}

	public float lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public float dot(Vector4f vector) {
		return x * vector.x + y * vector.y + z * vector.z + w * vector.w;
	}

	public float angle(Vector4f vector) {
		float dls = dot(vector) / (length() * vector.length());
		if (dls < -1f)
			dls = -1f;
		else if (dls > 1.0f)
			dls = 1.0f;
		return (float) Math.toDegrees(Math.acos(dls));
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

	public float w() {
		return w;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "Vector4f[" + x + ", " + y + ", " + z + ", " + w + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector4f other = (Vector4f) obj;
		return x == other.x && y == other.y && z == other.z && w == other.w;
	}

}
