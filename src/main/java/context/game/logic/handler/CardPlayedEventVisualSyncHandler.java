
package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;

public class CardPlayedEventVisualSyncHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private Queue<GameEvent> visualSync;

	public CardPlayedEventVisualSyncHandler(NomadsGameData data, Queue<GameEvent> visualSync) {
		this.data = data;
		this.visualSync = visualSync;
	}

	@Override
	public void accept(CardPlayedEvent event) {
		visualSync.add(event);
	}

}
