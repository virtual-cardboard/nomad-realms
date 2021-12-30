package event.game.playerinput;

import event.game.NomadRealmsGameEvent;

public abstract class NomadRealmsPlayerInputEvent extends NomadRealmsGameEvent {

	public NomadRealmsPlayerInputEvent() {
	}

	public NomadRealmsPlayerInputEvent(long time) {
		super(time);
	}

}
