package event.game.playerinput;

import event.NomadRealmsGameEvent;

public abstract class NomadRealmsPlayerInputEvent extends NomadRealmsGameEvent {

	public NomadRealmsPlayerInputEvent() {
	}

	public NomadRealmsPlayerInputEvent(long time) {
		super(time);
	}

}
