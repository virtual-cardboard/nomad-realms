package graphics.particle.function;

import java.util.function.IntFunction;

public interface ColourFunction extends IntFunction<Integer> {

	@Override
	Integer apply(int age);

}
