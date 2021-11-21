package event.game;

import common.event.GameEvent;
import common.source.GameSource;

public abstract class NomadRealmsGameEvent extends GameEvent {

	public NomadRealmsGameEvent() {
	}

	public NomadRealmsGameEvent(GameSource source) {
		super(source);
	}

	public NomadRealmsGameEvent(long time, GameSource source) {
		super(time, source);
	}

}
