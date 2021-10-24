package event.game;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;

public abstract class CardEffectEvent extends GameEvent {

	private static final long serialVersionUID = -6469495858393920360L;

	public CardEffectEvent(CardPlayer source) {
		super(source);
	}

	public abstract void process(GameState state);

}
