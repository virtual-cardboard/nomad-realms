package event.game.playerinput;

import static context.data.user.LocalUser.LOCAL_USER;
import static java.lang.System.currentTimeMillis;

import event.network.CardHoveredNetworkEvent;
import model.actor.CardPlayer;
import model.card.GameCard;

public class PlayerHoveredCardEvent extends NomadRealmsPlayerInputEvent {

	private CardPlayer player;
	private GameCard card;

	public PlayerHoveredCardEvent(CardPlayer player, GameCard card) {
		super(currentTimeMillis(), LOCAL_USER);
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
