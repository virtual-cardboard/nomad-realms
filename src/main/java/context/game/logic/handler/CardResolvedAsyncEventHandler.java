package context.game.logic.handler;

import java.util.function.Consumer;

import context.game.NomadsGameLogic;
import event.logicprocessing.CardResolvedAsyncEvent;
import event.logicprocessing.CardResolvedEvent;

public class CardResolvedAsyncEventHandler implements Consumer<CardResolvedAsyncEvent> {

	private final NomadsGameLogic logic;

	public CardResolvedAsyncEventHandler(NomadsGameLogic logic) {
		this.logic = logic;
	}

	/**
	 * Converts <code>cardResolvedAsyncEvent</code> to a {@link CardResolvedEvent}, then handles it.
	 *
	 * @param cardResolvedAsyncEvent the {@link CardResolvedAsyncEvent}
	 */
	@Override
	public void accept(CardResolvedAsyncEvent cardResolvedAsyncEvent) {
		logic.handleEvent(cardResolvedAsyncEvent.convertToCardResolvedEvent());
	}

}
