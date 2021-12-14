package graphics.particle.function;

public class DeceleratingMovementFunction implements MovementFunction {

	private float pos;
	private float vel;
	private float damp;

	public DeceleratingMovementFunction(float pos, float vel, float damp) {
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
