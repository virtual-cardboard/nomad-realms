package event.sync;

import event.NomadRealmsGameEvent;
import model.id.CardPlayerId;

public abstract class NomadRealmsSyncEvent extends NomadRealmsGameEvent {

	private CardPlayerId playerID;

	public NomadRealmsSyncEvent(CardPlayerId playerID) {
		this.playerID = playerID;
	}

	public final CardPlayerId playerID() {
		return playerID;
	}

}
