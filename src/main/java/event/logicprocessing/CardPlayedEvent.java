package event.logicprocessing;

import event.network.p2p.game.CardPlayedNetworkEvent;
import model.id.CardPlayerId;
import model.id.Id;
import model.id.WorldCardId;

public class CardPlayedEvent extends NomadRealmsLogicProcessingEvent {

	private Id targetId;
	private WorldCardId cardID;

	public CardPlayedEvent(CardPlayerId playerID, Id targetId, WorldCardId cardID) {
		super(playerID);
		this.targetId = targetId;
		this.cardID = cardID;
	}

	public CardPlayedNetworkEvent toNetworkEvent() {
		return new CardPlayedNetworkEvent(playerID().toLongID(), targetId != null ? targetId.toLongID() : 0, cardID.toLongID());
	}

	public Id targetID() {
		return targetId;
	}

	public WorldCardId cardID() {
		return cardID;
	}

}
