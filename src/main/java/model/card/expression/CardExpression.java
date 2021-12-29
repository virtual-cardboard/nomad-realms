package model.card.expression;

import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;
import model.state.GameState;

public abstract class CardExpression {

	public abstract void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain);

}
