package event.game.visualssync;

public class TookDamageSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long cardID;
	private int amount;

	public TookDamageSyncEvent(long playerID, long targetID, int amount) {
		super(playerID);
		this.cardID = targetID;
		this.amount = amount;
	}

	public long cardID() {
		return cardID;
	}

	public int amount() {
		return amount;
	}

}
