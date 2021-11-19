package event.game.logicprocessing.expression;

import event.game.logicprocessing.chain.ChainEvent;
import model.actor.CardPlayer;

public abstract class CardExpressionEvent extends ChainEvent {

	public CardExpressionEvent(CardPlayer source) {
		super(source);
	}

}
