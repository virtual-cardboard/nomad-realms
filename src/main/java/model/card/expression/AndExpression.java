package model.card.expression;

import model.GameState;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public class AndExpression extends CardExpression {

	private CardExpression cardEffect1;
	private CardExpression cardEffect2;

	public AndExpression(CardExpression cardEffect1, CardExpression cardEffect2) {
		this.cardEffect1 = cardEffect1;
		this.cardEffect2 = cardEffect2;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		cardEffect1.handle(playedBy, target, state, chain);
		cardEffect2.handle(playedBy, target, state, chain);
	}

}
