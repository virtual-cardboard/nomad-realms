package event.logicprocessing;

import event.NomadRealmsAsyncEvent;
import model.id.CardPlayerId;
import model.id.Id;
import model.id.WorldCardId;

public class CardResolvedAsyncEvent extends NomadRealmsAsyncEvent {

	private final CardPlayerId playerId;
	private final WorldCardId cardID;
	private final Id targetId;

	public CardResolvedAsyncEvent(long scheduledTick, CardPlayerId playerId, WorldCardId cardID, Id targetId) {
		super(scheduledTick);
		this.playerId = playerId;
		this.cardID = cardID;
		this.targetId = targetId;
	}

	public CardResolvedEvent convertToCardResolvedEvent() {
		return new CardResolvedEvent(playerId, cardID, targetId);
	}

}
