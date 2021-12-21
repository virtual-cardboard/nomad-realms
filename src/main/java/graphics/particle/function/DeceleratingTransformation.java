package graphics.particle.function;

public class DeceleratingTransformation implements ParticleTransformation {

	private float pos;
	private float vel;
	private float damp;

	/**
	 * @param pos  the starting position
	 * @param vel  the velocity
	 * @param damp the amount to damp the <code>vel</code> by each tick, usually
	 *             around 0.1f
	 */
	public DeceleratingTransformation(float pos, float vel, float damp) {
		this.pos = pos;
		this.vel = vel;
		this.damp = damp;
	}

	@Override
	public Float apply(int age) {
		return pos + (float) (vel * (Math.pow(1 - damp, age) - 1) / -damp);
	}

	public float velocity(int age) {
		return vel * (float) Math.pow(1 - damp, age);
	}

}
