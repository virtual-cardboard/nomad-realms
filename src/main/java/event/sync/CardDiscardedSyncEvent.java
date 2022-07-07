package event.sync;

import model.id.CardPlayerId;
import model.id.WorldCardId;

public class CardDiscardedSyncEvent extends NomadRealmsSyncEvent {

	private WorldCardId cardID;

	public CardDiscardedSyncEvent(CardPlayerId playerID, WorldCardId cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public WorldCardId cardID() {
		return cardID;
	}

}
