package graphics.particle.function;

import java.util.function.IntFunction;

@FunctionalInterface
public interface MovementFunction extends IntFunction<Float> {

	@Override
	Float apply(int age);

	default MovementFunction thenAdd(MovementFunction after) {
		return age -> apply(age) + after.apply(age);
	}

}
