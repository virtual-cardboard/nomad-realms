package event.sync;

import model.id.CardPlayerId;
import model.id.HealthActorId;

public class TookDamageSyncEvent extends NomadRealmsSyncEvent {

	private HealthActorId targetID;
	private int amount;

	public TookDamageSyncEvent(CardPlayerId playerID, HealthActorId targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	public HealthActorId targetID() {
		return targetID;
	}

	public int amount() {
		return amount;
	}

}
