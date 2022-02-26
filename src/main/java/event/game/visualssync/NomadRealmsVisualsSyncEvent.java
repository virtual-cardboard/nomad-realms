package event.game.visualssync;

import event.game.NomadRealmsGameEvent;
import model.actor.CardPlayer;
import model.id.ID;

public abstract class NomadRealmsVisualsSyncEvent extends NomadRealmsGameEvent {

	private ID<? extends CardPlayer> playerID;

	public NomadRealmsVisualsSyncEvent(ID<? extends CardPlayer> playerID) {
		this.playerID = playerID;
	}

	public final ID<? extends CardPlayer> playerID() {
		return playerID;
	}

}
