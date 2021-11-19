package event.game.logicprocessing;

import common.source.GameSource;
import event.game.NomadRealmsGameEvent;

public abstract class NomadRealmsLogicProcessingEvent extends NomadRealmsGameEvent {

	public NomadRealmsLogicProcessingEvent(GameSource source) {
		super(source);
	}

	public NomadRealmsLogicProcessingEvent(long time, GameSource source) {
		super(time, source);
	}

}
