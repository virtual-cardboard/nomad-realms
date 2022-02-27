package event.game.visualssync;

import model.id.CardPlayerID;
import model.id.WorldCardID;

public class CardResolvedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private WorldCardID cardID;

	public CardResolvedSyncEvent(CardPlayerID playerID, WorldCardID cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public WorldCardID cardID() {
		return cardID;
	}

}
