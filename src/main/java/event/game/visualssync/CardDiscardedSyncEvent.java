package event.game.visualssync;

import model.id.CardPlayerID;
import model.id.WorldCardID;

public class CardDiscardedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private WorldCardID cardID;

	public CardDiscardedSyncEvent(CardPlayerID playerID, WorldCardID cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public WorldCardID cardID() {
		return cardID;
	}

}
