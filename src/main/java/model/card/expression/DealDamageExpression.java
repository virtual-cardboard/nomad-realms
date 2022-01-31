package model.card.expression;

import model.card.chain.DealDamageEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class DealDamageExpression extends CardExpression {

	private int num;

	public DealDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new DealDamageEvent(playerID, targetID, num));
	}

}
