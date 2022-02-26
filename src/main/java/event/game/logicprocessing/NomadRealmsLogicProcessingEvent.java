package event.game.logicprocessing;

import event.game.NomadRealmsGameEvent;
import model.actor.CardPlayer;
import model.id.ID;

public abstract class NomadRealmsLogicProcessingEvent extends NomadRealmsGameEvent {

	private ID<? extends CardPlayer> playerID;

	public NomadRealmsLogicProcessingEvent(ID<? extends CardPlayer> playerID) {
		this.playerID = playerID;
	}

	public NomadRealmsLogicProcessingEvent(long time, ID<? extends CardPlayer> playerID) {
		super(time);
		this.playerID = playerID;
	}

	public final ID<? extends CardPlayer> playerID() {
		return playerID;
	}

}
