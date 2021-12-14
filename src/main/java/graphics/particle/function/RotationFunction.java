package graphics.particle.function;

import java.util.function.IntFunction;

public interface RotationFunction extends IntFunction<Float> {

	public static float PI = (float) Math.PI;

	@Override
	Float apply(int age);

	default RotationFunction thenAdd(RotationFunction after) {
		return age -> (apply(age) + after.apply(age) + 2 * PI) % (2 * PI);
	}

}
