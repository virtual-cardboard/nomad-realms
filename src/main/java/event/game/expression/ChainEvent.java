package event.game.expression;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;

public abstract class ChainEvent extends GameEvent {

	private static final long serialVersionUID = -6469495858393920360L;

	public ChainEvent(CardPlayer source) {
		super(source);
	}

	public abstract void process(GameState state);

}
