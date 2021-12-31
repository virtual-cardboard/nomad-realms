package event.game.visualssync;

public class CardShuffledSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long cardID;

	public CardShuffledSyncEvent(long playerID, long cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public long cardID() {
		return cardID;
	}

}
