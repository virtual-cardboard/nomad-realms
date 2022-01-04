package event.game.logicprocessing;

import event.game.NomadRealmsGameEvent;

public abstract class NomadRealmsLogicProcessingEvent extends NomadRealmsGameEvent {

	private long playerID;

	public NomadRealmsLogicProcessingEvent(long playerID) {
		this.playerID = playerID;
	}

	public NomadRealmsLogicProcessingEvent(long time, long playerID) {
		super(time);
		this.playerID = playerID;
	}

	public final long playerID() {
		return playerID;
	}

}
