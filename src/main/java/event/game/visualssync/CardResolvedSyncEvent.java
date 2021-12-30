package event.game.visualssync;

import model.actor.CardPlayer;
import model.card.GameCard;

public class CardResolvedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private GameCard card;

	public CardResolvedSyncEvent(CardPlayer player, GameCard card) {
		super(player);
		this.card = card;
	}

	public GameCard card() {
		return card;
	}

}
