package model.card.effect;

import model.GameState;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public abstract class CardExpression {

	public abstract void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain);

}
