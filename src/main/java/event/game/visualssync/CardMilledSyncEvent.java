package event.game.visualssync;

import model.actor.CardPlayer;
import model.id.ID;
import model.id.WorldCardID;

public class CardMilledSyncEvent extends NomadRealmsVisualsSyncEvent {

	private ID<?> targetID;
	private WorldCardID cardID;

	public CardMilledSyncEvent(ID<? extends CardPlayer> playerID, ID<?> targetID, WorldCardID cardID) {
		super(playerID);
		this.targetID = targetID;
		this.cardID = cardID;
	}

	public ID<?> targetID() {
		return targetID;
	}

	public WorldCardID cardID() {
		return cardID;
	}

}
