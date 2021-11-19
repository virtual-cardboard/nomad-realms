package event.game.logicprocessing.expression;

import common.source.GameSource;
import event.game.logicprocessing.chain.ChainEvent;
import model.actor.CardPlayer;

public abstract class CardExpressionEvent extends ChainEvent {

	public CardExpressionEvent(CardPlayer source) {
		super(source);
	}

	public CardExpressionEvent(long time, GameSource source) {
		super(time, source);
	}

}
