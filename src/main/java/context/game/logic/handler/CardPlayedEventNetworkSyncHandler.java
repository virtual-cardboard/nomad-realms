
package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import event.game.logicprocessing.CardPlayedEvent;

public class CardPlayedEventNetworkSyncHandler implements Consumer<CardPlayedEvent> {

	private Queue<GameEvent> networkSync;

	public CardPlayedEventNetworkSyncHandler(Queue<GameEvent> networkSync) {
		this.networkSync = networkSync;
	}

	@Override
	public void accept(CardPlayedEvent event) {
		networkSync.add(event.toNetworkEvent());
	}

}
