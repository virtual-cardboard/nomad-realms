package model.card.expression;

import model.card.chain.DrawCardEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class SelfDrawCardExpression extends CardExpression {

	private int amount;

	public SelfDrawCardExpression() {
		this(1);
	}

	public SelfDrawCardExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.add(new DrawCardEvent(playerID, playerID, amount));
	}

}
