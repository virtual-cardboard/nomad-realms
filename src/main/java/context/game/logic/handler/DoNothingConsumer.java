package context.game.logic.handler;

import java.util.function.Consumer;

public class DoNothingConsumer<T> implements Consumer<T> {

	@Override
	public void accept(T t) {
	}

}
