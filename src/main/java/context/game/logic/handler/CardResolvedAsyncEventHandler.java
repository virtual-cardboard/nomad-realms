package context.game.logic.handler;

import java.util.function.Consumer;

import engine.common.event.GameEvent;
import event.logicprocessing.CardResolvedAsyncEvent;
import event.logicprocessing.CardResolvedEvent;

public class CardResolvedAsyncEventHandler implements Consumer<CardResolvedAsyncEvent> {

	private final Consumer<GameEvent> handleCallback;

	public CardResolvedAsyncEventHandler(Consumer<GameEvent> handleCallback) {
		this.handleCallback = handleCallback;
	}

	/**
	 * Converts <code>cardResolvedAsyncEvent</code> to a {@link CardResolvedEvent}, then handles it.
	 *
	 * @param cardResolvedAsyncEvent the {@link CardResolvedAsyncEvent}
	 */
	@Override
	public void accept(CardResolvedAsyncEvent cardResolvedAsyncEvent) {
		handleCallback.accept(cardResolvedAsyncEvent.convertToCardResolvedEvent());
	}

}
