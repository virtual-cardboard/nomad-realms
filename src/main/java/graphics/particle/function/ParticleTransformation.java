package graphics.particle.function;

import java.util.function.IntFunction;

@FunctionalInterface
public interface ParticleTransformation extends IntFunction<Float> {

	@Override
	Float apply(int age);

	default ParticleTransformation thenAdd(ParticleTransformation after) {
		return age -> apply(age) + after.apply(age);
	}

}
