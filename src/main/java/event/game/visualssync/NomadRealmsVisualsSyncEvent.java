package event.game.visualssync;

import common.source.GameSource;
import event.game.NomadRealmsGameEvent;

public abstract class NomadRealmsVisualsSyncEvent extends NomadRealmsGameEvent {

	public NomadRealmsVisualsSyncEvent() {
		super(null);
	}

	public NomadRealmsVisualsSyncEvent(GameSource source) {
		super(source);
	}

	public NomadRealmsVisualsSyncEvent(long time, GameSource source) {
		super(time, source);
	}

}
