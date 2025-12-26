package engine.common.math;

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
	 * @param axis  the axis of rotation
	 * @param theta the angle of rotation in radians
	 */
	public UnitQuaternion(Vector3f axis, float theta) {
		double angle = Math.toRadians(theta);
		axis = axis.normalise().scale((float) Math.sin(angle));
		setComponents((float) Math.cos(angle), axis.x(), axis.y(), axis.z());
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
		float magnitude = w * w + x * x + y * y + z * z;
		this.w = w / magnitude;
		this.x = x / magnitude;
		this.y = y / magnitude;
		this.z = z / magnitude;
		return this;
	}

	public Vector3f getAxis() {
		Vector3f axis = new Vector3f(x, y, z);
		if (axis.lengthSquared() == 0) {
			axis = new Vector3f(0, 1, 0);
		}
		return axis.normalise();
	}

	public float getAngle() {
		// acos(w) returns the half-angle. We must multiply by 2 to get the full rotation.
		// We also clamp w to [-1, 1] to prevent NaN results from floating point drift.
		float val = Math.max(-1.0f, Math.min(1.0f, w));
		return (float) Math.toDegrees(2 * Math.acos(val));
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
	 * Multiplies the corresponding scalar parts and sums the results.
	 *
	 * @param q the other quaternion
	 * @return the dot product
	 */
	public float dot(UnitQuaternion q) {
		float sum = w * q.w + x * q.x + y * q.y + z * q.z;
		return sum;
	}

	/**
	 * Computes the angular difference between the quaternions.
	 *
	 * @param q the other quaternion
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
		float s = w * q.w - getV().dot(q.getV());
		Vector3f saB = q.getV().scale(w);
		Vector3f sbA = getV().scale(q.w);
		Vector3f cross = getV().cross(q.getV());
		Vector3f v = saB.add(sbA).add(cross);
		return new UnitQuaternion(s, v.x(), v.y(), v.z());
	}

	public UnitQuaternion rotateBy(UnitQuaternion rotation) {
		return this.multiply(rotation);
	}

	public UnitQuaternion rotateBy(Vector3f axisOfRotation, float angle) {
		return rotateBy(new UnitQuaternion(axisOfRotation, angle / 2));
	}

	public Vector3f rotateVector3f(Vector3f vector) {
		UnitQuaternion conjugate = this.getConjugate();
		Quaternion pureQuaternion = new Quaternion(0, vector.x(), vector.y(), vector.z());
		Quaternion resultantQuaternion = this.multiply(pureQuaternion).multiply(conjugate);
		return resultantQuaternion.getV();
	}

	@Override
	public String toString() {
		return "UnitQuaternion: [" + w + ", " + x + ", " + y + ", " + z + "]";
	}

}