package event.game;

import static context.data.user.LocalUser.LOCAL_USER;
import static java.lang.System.currentTimeMillis;

import model.actor.CardPlayer;

public class CardHoveredEvent extends CardMovementEvent {

	private static final long serialVersionUID = -7897388072148113785L;

	private CardPlayer player;

	public CardHoveredEvent(CardPlayer player) {
		super(currentTimeMillis(), LOCAL_USER);
		this.player = player;
	}

	public CardPlayer player() {
		return player;
	}

}
