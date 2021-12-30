package event.game.logicprocessing;

import event.game.NomadRealmsGameEvent;

public abstract class NomadRealmsLogicProcessingEvent extends NomadRealmsGameEvent {

	public NomadRealmsLogicProcessingEvent() {
	}

	public NomadRealmsLogicProcessingEvent(long time) {
		super(time);
	}

}
