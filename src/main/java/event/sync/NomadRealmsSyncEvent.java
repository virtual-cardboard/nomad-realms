package event.sync;

import event.NomadRealmsGameEvent;
import model.id.CardPlayerID;

public abstract class NomadRealmsSyncEvent extends NomadRealmsGameEvent {

	private CardPlayerID playerID;

	public NomadRealmsSyncEvent(CardPlayerID playerID) {
		this.playerID = playerID;
	}

	public final CardPlayerID playerID() {
		return playerID;
	}

}
