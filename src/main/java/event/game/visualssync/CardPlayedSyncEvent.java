package event.game.visualssync;

import math.WorldPos;
import model.id.CardPlayerID;
import model.id.WorldCardID;

public class CardPlayedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private WorldCardID cardID;
	private WorldPos pos;

	public CardPlayedSyncEvent(CardPlayerID playerID, WorldCardID cardID, WorldPos pos) {
		super(playerID);
		this.cardID = cardID;
		this.pos = pos;
	}

	public WorldCardID cardID() {
		return cardID;
	}

	public WorldPos pos() {
		return pos;
	}

}
