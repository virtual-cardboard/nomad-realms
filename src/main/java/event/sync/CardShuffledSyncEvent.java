package event.sync;

import model.id.CardPlayerId;
import model.id.WorldCardId;

public class CardShuffledSyncEvent extends NomadRealmsSyncEvent {

	private WorldCardId cardID;

	public CardShuffledSyncEvent(CardPlayerId playerID, WorldCardId cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public WorldCardId cardID() {
		return cardID;
	}

}
