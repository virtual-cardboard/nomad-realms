package math;

import common.math.Vector3f;

public class UnitQuaternion extends Quaternion {

	public UnitQuaternion() {
	}

	public UnitQuaternion(float w, float x, float y, float z) {
		setComponents(w, x, y, z);
	}

	public UnitQuaternion(Quaternion q) {
		setComponents(q.w, q.x, q.y, q.z);
	}

	/**
	 * Creates a unit quaternion with axis and angle as specified.
	 * 
	 */
	public UnitQuaternion(Vector3f axis, float theta) {
		double angle = Math.toRadians(theta);
		this.w = (float) Math.cos(angle);
		axis.normalise();
		axis.scale((float) Math.sin(angle));
		this.x = axis.x;
		this.y = axis.y;
		this.z = axis.z;
		normalize();
	}

	public UnitQuaternion(UnitQuaternion q) {
		this.w = q.w;
		this.x = q.x;
		this.y = q.y;
		this.z = q.z;
	}

	/**
	 * Sets the unit quaternion through individual components.
	 * 
	 * @param w
	 * @param x
	 * @param y
	 * @param z
	 * @return this quaternion
	 */
	private UnitQuaternion setComponents(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
		normalize();
		return this;
	}

	public Vector3f getAxis() {
		Vector3f axis = new Vector3f(x, y, z);
		if (axis.lengthSquared() == 0) {
			axis.set(0, 1, 0);
		}
		axis.normalise();
		return axis;
	}

	public float getAngle() {
		return (float) Math.toDegrees(Math.acos(w));
	}

	@Override
	public UnitQuaternion getConjugate() {
		return new UnitQuaternion(w, -x, -y, -z);
	}

	@Override
	public UnitQuaternion getInverse() {
		return getConjugate();
	}

	/**
	 * 
	 * Multiplies the corresponding scalar parts and sums the results.
	 * 
	 * @param quaternion
	 * @return the dot product
	 */
	public float dot(UnitQuaternion q) {
		float sum = w * q.w + x * q.x + y * q.y + z * q.z;
		return sum;
	}

	/**
	 * Computes the angular difference between the quaternions.
	 * 
	 * @param quaternion
	 * @return the angle difference
	 */
	public float angleBetween(UnitQuaternion q) {
		float cosTheta = this.dot(q) / (magnitude() * q.magnitude());
		float theta = (float) Math.acos(cosTheta);
		return theta;
	}

	/**
	 * Multiplies this quaternion by the parameter quaternion. What this effectively
	 * does is transforms the current quaternion by the given quaternion. The given
	 * quaternion is treated as a rotation.
	 * 
	 * @param q the rotation quaternion
	 * @return the resultant quaternion
	 */
	public UnitQuaternion multiply(UnitQuaternion q) {
		float s = w * q.w - Vector3f.dot(getV(), q.getV());
		Vector3f saB = q.getV().scale(w);
		Vector3f sbA = getV().scale(q.w);
		Vector3f cross = Vector3f.cross(getV(), q.getV());
		Vector3f v = Vector3f.add(Vector3f.add(saB, sbA), cross);
		return new UnitQuaternion(s, v.x, v.y, v.z);
	}

	public UnitQuaternion rotateBy(UnitQuaternion rotation) {
		return this.multiply(rotation);
	}

	public UnitQuaternion rotateBy(Vector3f axisOfRotation, float angle) {
		return rotateBy(new UnitQuaternion(axisOfRotation, angle / 2));
	}

	public Vector3f rotateVector3f(Vector3f vector) {
		UnitQuaternion conjugate = this.getConjugate();
		Quaternion pureQuaternion = new Quaternion(0, vector.x, vector.y, vector.z);
		Quaternion resultantQuaternion = this.multiply(pureQuaternion).multiply(conjugate);
		Vector3f result = resultantQuaternion.getV();
		return result;
	}

	@Override
	public String toString() {
		return "UnitQuaternion: [" + w + ", " + x + ", " + y + ", " + z + "]";
	}
}
