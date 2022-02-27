package event.game.logicprocessing;

import model.id.CardPlayerID;
import model.id.ID;
import model.id.WorldCardID;

public class CardResolvedEvent extends NomadRealmsLogicProcessingEvent {

	private WorldCardID cardID;
	private ID targetID;

	public CardResolvedEvent(CardPlayerID playerID, WorldCardID cardID, ID targetID) {
		super(playerID);
		this.cardID = cardID;
		this.targetID = targetID;
	}

	public WorldCardID cardID() {
		return cardID;
	}

	public ID targetID() {
		return targetID;
	}

}
