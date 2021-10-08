package event.game;

import common.event.GameEvent;
import common.source.GameSource;

public class DrawCardEvent extends GameEvent {

	public DrawCardEvent(GameSource source) {
		super(source);
	}

}
