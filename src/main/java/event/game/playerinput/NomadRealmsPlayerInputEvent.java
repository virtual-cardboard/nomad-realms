package event.game.playerinput;

import common.source.GameSource;
import event.game.NomadRealmsGameEvent;

public abstract class NomadRealmsPlayerInputEvent extends NomadRealmsGameEvent {

	public NomadRealmsPlayerInputEvent(GameSource source) {
		super(source);
	}

	public NomadRealmsPlayerInputEvent(long time, GameSource source) {
		super(time, source);
	}

}
