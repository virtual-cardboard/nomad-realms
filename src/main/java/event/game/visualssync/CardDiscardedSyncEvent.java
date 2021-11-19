package event.game.visualssync;

import model.actor.CardPlayer;
import model.card.GameCard;

public class CardDiscardedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private GameCard card;

	public CardDiscardedSyncEvent(CardPlayer player, GameCard card) {
		super(player);
		this.card = card;
	}

	public CardPlayer player() {
		return (CardPlayer) source();
	}

	public GameCard card() {
		return card;
	}

}
