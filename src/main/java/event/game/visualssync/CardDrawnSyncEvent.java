package event.game.visualssync;

public class CardDrawnSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long targetID;
	private long cardID;

	public CardDrawnSyncEvent(long playerID, long targetID, long cardID) {
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
