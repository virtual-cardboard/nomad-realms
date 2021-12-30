package event.game.visualssync;

import event.game.NomadRealmsGameEvent;
import model.actor.CardPlayer;

public abstract class NomadRealmsVisualsSyncEvent extends NomadRealmsGameEvent {

	private CardPlayer player;

	public NomadRealmsVisualsSyncEvent() {
		this(null);
	}

	public NomadRealmsVisualsSyncEvent(CardPlayer player) {
		this.player = player;
	}

	public final CardPlayer player() {
		return player;
	}

}
