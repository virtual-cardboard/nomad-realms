package event.game.visualssync;

import event.game.NomadRealmsGameEvent;

public abstract class NomadRealmsVisualsSyncEvent extends NomadRealmsGameEvent {

	private long playerID;

	public NomadRealmsVisualsSyncEvent(long playerID) {
		this.playerID = playerID;
	}

	public final long playerID() {
		return playerID;
	}

}
