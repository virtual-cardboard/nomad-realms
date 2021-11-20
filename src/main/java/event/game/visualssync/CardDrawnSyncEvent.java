package event.game.visualssync;

import model.actor.CardPlayer;
import model.card.GameCard;

public class CardDrawnSyncEvent extends NomadRealmsVisualsSyncEvent {

	private GameCard card;
	private CardPlayer target;

	public CardDrawnSyncEvent(CardPlayer player, CardPlayer target, GameCard card) {
		super(player);
		this.target = target;
		this.card = card;
	}

	public CardPlayer player() {
		return (CardPlayer) source();
	}

	public CardPlayer target() {
		return target;
	}

	public GameCard card() {
		return card;
	}

}
