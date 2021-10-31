package event.game.expression;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;

public abstract class CardExpressionEvent extends GameEvent {

	private static final long serialVersionUID = -6469495858393920360L;

	public CardExpressionEvent(CardPlayer source) {
		super(source);
	}

	public abstract void process(GameState state);

}
