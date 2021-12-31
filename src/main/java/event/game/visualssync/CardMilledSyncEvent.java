package event.game.visualssync;

public class CardMilledSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long targetID;
	private long cardID;

	public CardMilledSyncEvent(long playerID, long targetID, long cardID) {
		super(playerID);
		this.targetID = targetID;
		this.cardID = cardID;
	}

	public long targetID() {
		return targetID;
	}

	public long cardID() {
		return cardID;
	}

}
