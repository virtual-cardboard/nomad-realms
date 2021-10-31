package event.game;

import static context.data.user.LocalUser.LOCAL_USER;
import static java.lang.System.currentTimeMillis;

import model.actor.CardPlayer;
import model.card.GameCard;

public class CardHoveredEvent extends CardMovementEvent {

	private static final long serialVersionUID = -7897388072148113785L;

	private CardPlayer player;
	private GameCard card;

	public CardHoveredEvent(CardPlayer player, GameCard card) {
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

}
