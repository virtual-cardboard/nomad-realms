package event.game.visualssync;

import model.actor.CardPlayer;
import model.actor.HealthActor;
import model.id.ID;

public class TookDamageSyncEvent extends NomadRealmsVisualsSyncEvent {

	private ID<? extends HealthActor> targetID;
	private int amount;

	public TookDamageSyncEvent(ID<? extends CardPlayer> playerID, ID<? extends HealthActor> targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	public ID<? extends HealthActor> targetID() {
		return targetID;
	}

	public int amount() {
		return amount;
	}

}
