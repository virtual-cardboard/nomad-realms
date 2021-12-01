
package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import event.game.playerinput.PlayerHoveredCardEvent;

public class CardHoveredEventHandler implements Consumer<PlayerHoveredCardEvent> {

	private Queue<GameEvent> sync;

	public CardHoveredEventHandler(Queue<GameEvent> sync) {
		this.sync = sync;
	}

	@Override
	public void accept(PlayerHoveredCardEvent event) {
		sync.add(event);
	}

}
