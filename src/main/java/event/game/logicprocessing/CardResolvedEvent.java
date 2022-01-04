package event.game.logicprocessing;

public class CardResolvedEvent extends NomadRealmsLogicProcessingEvent {

	private long cardID;
	private long targetID;

	public CardResolvedEvent(long playerID, long cardID, long targetID) {
		super(playerID);
		this.cardID = cardID;
		this.targetID = targetID;
	}

	public long cardID() {
		return cardID;
	}

	public long targetID() {
		return targetID;
	}

}
