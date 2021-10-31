package event.game;

import common.event.GameEvent;
import common.source.GameSource;

public class CardMovementEvent extends GameEvent {

	private static final long serialVersionUID = 441709034033651243L;

	public CardMovementEvent(long time, GameSource source) {
		super(time, source);
	}

	public CardMovementEvent(GameSource source) {
		super(source);
	}

}
