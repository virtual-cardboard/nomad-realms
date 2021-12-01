
package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.visualssync.CardPlayedSyncEvent;

public class CardPlayedEventVisualSyncHandler implements Consumer<CardPlayedEvent> {

	private Queue<GameEvent> visualSync;

	public CardPlayedEventVisualSyncHandler(Queue<GameEvent> visualSync) {
		this.visualSync = visualSync;
	}

	@Override
	public void accept(CardPlayedEvent event) {
		visualSync.add(new CardPlayedSyncEvent(event.player(), event.card()));
	}

}
