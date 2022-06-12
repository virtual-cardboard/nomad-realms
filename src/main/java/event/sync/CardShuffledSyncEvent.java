package event.sync;

import model.id.CardPlayerID;
import model.id.WorldCardID;

public class CardShuffledSyncEvent extends NomadRealmsSyncEvent {

	private WorldCardID cardID;

	public CardShuffledSyncEvent(CardPlayerID playerID, WorldCardID cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public WorldCardID cardID() {
		return cardID;
	}

}
