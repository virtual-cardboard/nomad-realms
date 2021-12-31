package event.game.visualssync;

public class CardResolvedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long cardID;

	public CardResolvedSyncEvent(long playerID, long cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public long cardID() {
		return cardID;
	}

}
