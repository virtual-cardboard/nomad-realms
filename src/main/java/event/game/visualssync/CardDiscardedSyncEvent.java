package event.game.visualssync;

public class CardDiscardedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long cardID;

	public CardDiscardedSyncEvent(long playerID, long cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public long cardID() {
		return cardID;
	}

}
