package model.card.expression;

import model.chain.EffectChain;
import model.state.GameState;

public abstract class CardExpression {

	public abstract void handle(long playerID, long targetID, GameState state, EffectChain chain);

}
