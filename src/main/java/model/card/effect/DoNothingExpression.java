package model.card.effect;

import model.GameState;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public class DoNothingExpression extends CardExpression {

	@Override
	public void handle(CardPlayer playedBy, Actor target, GameState state, EffectChain chain) {
	}

}
