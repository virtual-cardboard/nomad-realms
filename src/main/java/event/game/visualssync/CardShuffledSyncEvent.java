package event.game.visualssync;

import model.actor.CardPlayer;
import model.id.ID;
import model.id.WorldCardID;

public class CardShuffledSyncEvent extends NomadRealmsVisualsSyncEvent {

	private WorldCardID cardID;

	public CardShuffledSyncEvent(ID<? extends CardPlayer> playerID, WorldCardID cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	public WorldCardID cardID() {
		return cardID;
	}

}
