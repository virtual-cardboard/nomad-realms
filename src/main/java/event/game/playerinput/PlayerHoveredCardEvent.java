package event.game.playerinput;

import event.network.game.CardHoveredNetworkEvent;
import model.actor.CardPlayer;
import model.card.GameCard;

public class PlayerHoveredCardEvent extends NomadRealmsPlayerInputEvent {

	private CardPlayer player;
	private GameCard card;

	public PlayerHoveredCardEvent(CardPlayer player, GameCard card) {
		this.player = player;
		this.card = card;
	}

	public CardPlayer player() {
		return player;
	}

	public GameCard card() {
		return card;
	}

	public CardHoveredNetworkEvent toNetworkEvent() {
		return new CardHoveredNetworkEvent(time(), player.id(), card.id());
	}

}
