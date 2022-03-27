package model.world;

import java.util.function.Function;

@FunctionalInterface
public interface BiDoubleFunction<T> {

	T apply(double a, double b);

	default <V> BiDoubleFunction<V> andThen(Function<? super T, ? extends V> after) {
		return (a, b) -> after.apply(apply(a, b));
	}

}
