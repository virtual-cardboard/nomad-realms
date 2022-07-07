package event.sync;

import model.id.CardPlayerId;
import model.id.Id;
import model.id.WorldCardId;

public class CardMilledSyncEvent extends NomadRealmsSyncEvent {

	private Id targetId;
	private WorldCardId cardID;

	public CardMilledSyncEvent(CardPlayerId playerID, Id targetId, WorldCardId cardID) {
		super(playerID);
		this.targetId = targetId;
		this.cardID = cardID;
	}

	public Id targetID() {
		return targetId;
	}

	public WorldCardId cardID() {
		return cardID;
	}

}
