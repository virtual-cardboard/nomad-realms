package util.when.util.key;

import java.util.function.Function;
import java.util.function.Supplier;

import context.input.event.AbstractKeyInputEvent;

public class AbstractWhenKey<T extends AbstractKeyInputEvent> {

	private final int key;

	public AbstractWhenKey(int key) {
		this.key = key;
	}

	public <R> Function<T, R> then(Supplier<R> result) {
		return event -> {
			if (event.code() == key) {
				return result.get();
			}
			return null;
		};
	}

	public <R> Function<T, R> thenReturn(R result) {
		return event -> {
			if (event.code() == key) {
				return result;
			}
			return null;
		};
	}

}
