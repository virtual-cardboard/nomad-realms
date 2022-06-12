package event.sync;

import model.id.CardPlayerID;
import model.id.WorldCardID;

public class CardDiscardedSyncEvent extends NomadRealmsSyncEvent {

	private WorldCardID cardID;

	public CardDiscardedSyncEvent(CardPlayerID playerID, WorldCardID cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public WorldCardID cardID() {
		return cardID;
	}

}
