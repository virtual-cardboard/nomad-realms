package event.logicprocessing;

import event.NomadRealmsGameEvent;
import model.id.CardPlayerId;

public abstract class NomadRealmsLogicProcessingEvent extends NomadRealmsGameEvent {

	private final CardPlayerId playerID;

	public NomadRealmsLogicProcessingEvent(CardPlayerId playerID) {
		this.playerID = playerID;
	}

	public final CardPlayerId playerID() {
		return playerID;
	}

}
