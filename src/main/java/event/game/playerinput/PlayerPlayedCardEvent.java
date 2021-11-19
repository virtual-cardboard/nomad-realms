package event.game.playerinput;

import common.source.GameSource;

public class PlayerPlayedCardEvent extends NomadRealmsPlayerInputEvent {

	public PlayerPlayedCardEvent(GameSource source) {
		super(source);
	}

	public PlayerPlayedCardEvent(long time, GameSource source) {
		super(time, source);
	}

}
