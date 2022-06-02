package event.game.logicprocessing;

import event.NomadRealmsGameEvent;
import model.id.CardPlayerID;

public abstract class NomadRealmsLogicProcessingEvent extends NomadRealmsGameEvent {

	private final CardPlayerID playerID;

	public NomadRealmsLogicProcessingEvent(CardPlayerID playerID) {
		this.playerID = playerID;
	}

	public final CardPlayerID playerID() {
		return playerID;
	}

}
