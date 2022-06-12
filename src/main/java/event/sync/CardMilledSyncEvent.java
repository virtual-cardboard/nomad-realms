package event.sync;

import model.id.CardPlayerID;
import model.id.ID;
import model.id.WorldCardID;

public class CardMilledSyncEvent extends NomadRealmsSyncEvent {

	private ID targetID;
	private WorldCardID cardID;

	public CardMilledSyncEvent(CardPlayerID playerID, ID targetID, WorldCardID cardID) {
		super(playerID);
		this.targetID = targetID;
		this.cardID = cardID;
	}

	public ID targetID() {
		return targetID;
	}

	public WorldCardID cardID() {
		return cardID;
	}

}
