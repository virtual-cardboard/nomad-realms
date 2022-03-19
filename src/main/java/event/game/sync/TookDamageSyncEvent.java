package event.game.sync;

import model.id.CardPlayerID;
import model.id.HealthActorID;

public class TookDamageSyncEvent extends NomadRealmsSyncEvent {

	private HealthActorID targetID;
	private int amount;

	public TookDamageSyncEvent(CardPlayerID playerID, HealthActorID targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	public HealthActorID targetID() {
		return targetID;
	}

	public int amount() {
		return amount;
	}

}