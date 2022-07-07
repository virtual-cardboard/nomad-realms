package event.logicprocessing;

import model.id.CardPlayerId;
import model.id.Id;
import model.id.WorldCardId;

public class CardResolvedEvent extends NomadRealmsLogicProcessingEvent {

	private WorldCardId cardID;
	private Id targetId;

	public CardResolvedEvent(CardPlayerId playerID, WorldCardId cardID, Id targetId) {
		super(playerID);
		this.cardID = cardID;
		this.targetId = targetId;
	}

	public WorldCardId cardID() {
		return cardID;
	}

	public Id targetID() {
		return targetId;
	}

}
