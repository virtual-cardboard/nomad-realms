package graphics.particle.function;

public class DeceleratingRotationFunction implements RotationFunction {

	private float rot;
	private float vel;
	private float damp;

	public DeceleratingRotationFunction(float rot, float vel, float damp) {
		this.rot = (rot + 2 * PI) % (2 * PI);
		this.vel = (vel + 2 * PI) % (2 * PI);
		this.damp = damp;
	}

	@Override
	public Float apply(int age) {
		return (rot + (float) (vel * (Math.pow(1 - damp, age) - 1) / -damp) + 2 * PI) % (2 * PI);
	}

}
