package event.game;

import static context.data.user.LocalUser.LOCAL_USER;
import static java.lang.System.currentTimeMillis;

import model.actor.CardPlayer;

public class CardDrawTickEvent extends CardMovementEvent {

	private static final long serialVersionUID = -5498891422754809199L;

	private CardPlayer player;

	public CardDrawTickEvent(CardPlayer player) {
		super(currentTimeMillis(), LOCAL_USER);
		this.player = player;
	}

	public CardPlayer player() {
		return player;
	}

}
