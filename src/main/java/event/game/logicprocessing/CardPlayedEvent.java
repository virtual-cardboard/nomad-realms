package event.game.logicprocessing;

import event.network.game.CardPlayedNetworkEvent;

public class CardPlayedEvent extends NomadRealmsLogicProcessingEvent {

	private long cardID;
	private long targetID;

	public CardPlayedEvent(long playerID, long cardID, long targetID) {
		super(playerID);
		this.cardID = cardID;
		this.targetID = targetID;
	}

	public CardPlayedNetworkEvent toNetworkEvent() {
		return new CardPlayedNetworkEvent(time(), playerID(), targetID, cardID());
	}

	public long cardID() {
		return cardID;
	}

	public long targetID() {
		return targetID;
	}

}
