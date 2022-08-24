package event.logicprocessing;

import static app.NomadRealmsClient.DEBUG;

import model.card.GameCard;
import model.card.WorldCard;
import model.id.CardPlayerId;
import model.id.Id;
import model.id.WorldCardId;

public class CardResolvedEvent extends NomadRealmsLogicProcessingEvent {

	private WorldCardId cardID;
	private Id targetId;

	// For debugging purposes
	private transient GameCard card;

	public CardResolvedEvent(CardPlayerId playerID, WorldCardId cardID, Id targetId) {
		super(playerID);
		this.cardID = cardID;
		this.targetId = targetId;
	}

	public CardResolvedEvent(CardPlayerId playerID, WorldCard card, Id targetId) {
		super(playerID);
		this.cardID = card.id();
		this.targetId = targetId;
		if (DEBUG) {
			this.card = card.card();
		}
	}

	public CardResolvedEvent(CardPlayerId playerID, WorldCardId cardID, GameCard card, Id targetId) {
		super(playerID);
		this.cardID = cardID;
		this.targetId = targetId;
		if (DEBUG) {
			this.card = card;
		}
	}

	public WorldCardId cardID() {
		return cardID;
	}

	public Id targetID() {
		return targetId;
	}

	/**
	 * This function should only be called for debugging purposes.
	 * <p>
	 * Returns the {@link GameCard} that was resolved. This method is only available if
	 * {@link app.NomadRealmsClient#DEBUG DEBUG} is true.
	 *
	 * @return the card that was resolved
	 */
	public GameCard card() {
		if (DEBUG) {
			return card;
		}
		throw new IllegalStateException("This method is only available if DEBUG is true");
	}

}
