package model.card.expression;

import model.GameState;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public class DoNothingExpression extends CardExpression {

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
	}

}
