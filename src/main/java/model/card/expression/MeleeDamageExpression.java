package model.card.expression;

import model.card.chain.MeleeDamageEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class MeleeDamageExpression extends CardExpression {

	private int num;

	public MeleeDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new MeleeDamageEvent(playerID, targetID, num));
	}

}
