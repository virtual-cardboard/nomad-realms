package event.game.logicprocessing;

import event.game.NomadRealmsGameEvent;
import model.id.CardPlayerID;

public abstract class NomadRealmsLogicProcessingEvent extends NomadRealmsGameEvent {

	private CardPlayerID playerID;

	public NomadRealmsLogicProcessingEvent(CardPlayerID playerID) {
		this.playerID = playerID;
	}

	public NomadRealmsLogicProcessingEvent(long time, CardPlayerID playerID) {
		super(time);
		this.playerID = playerID;
	}

	public final CardPlayerID playerID() {
		return playerID;
	}

}
