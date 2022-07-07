package event.sync;

import model.id.CardPlayerId;
import model.id.Id;
import model.id.WorldCardId;

public class CardDrawnSyncEvent extends NomadRealmsSyncEvent {

	private Id targetId;
	private WorldCardId cardID;

	public CardDrawnSyncEvent(CardPlayerId playerID, Id targetId, WorldCardId cardID) {
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
