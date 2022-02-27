package event.game.visualssync;

import event.game.NomadRealmsGameEvent;
import model.id.CardPlayerID;

public abstract class NomadRealmsVisualsSyncEvent extends NomadRealmsGameEvent {

	private CardPlayerID playerID;

	public NomadRealmsVisualsSyncEvent(CardPlayerID playerID) {
		this.playerID = playerID;
	}

	public final CardPlayerID playerID() {
		return playerID;
	}

}
