package model.card.expression;

import model.chain.EffectChain;
import model.state.GameState;

public class DoNothingExpression extends CardExpression {

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
	}

}
