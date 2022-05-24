package event.game.logicprocessing;

import event.network.p2p.bootstrap.game.CardPlayedNetworkEvent;
import model.id.CardPlayerID;
import model.id.ID;
import model.id.WorldCardID;

public class CardPlayedEvent extends NomadRealmsLogicProcessingEvent {

	private ID targetID;
	private WorldCardID cardID;

	public CardPlayedEvent(CardPlayerID playerID, ID targetID, WorldCardID cardID) {
		super(playerID);
		this.targetID = targetID;
		this.cardID = cardID;
	}

	public CardPlayedNetworkEvent toNetworkEvent() {
		return new CardPlayedNetworkEvent(time(), playerID().toLongID(), targetID != null ? targetID.toLongID() : 0, cardID.toLongID());
	}

	public ID targetID() {
		return targetID;
	}

	public WorldCardID cardID() {
		return cardID;
	}

}
