package graphics.particle.function;

public class LinearMovementFunction implements MovementFunction {

	private float pos;
	private float vel;
	private float acc;

	public LinearMovementFunction(float pos, float vel, float acc) {
		this.pos = pos;
		this.vel = vel;
		this.acc = acc;
	}

	@Override
	public Float apply(int age) {
		return pos + vel * age + acc * 0.5f * age * age;
	}

}
