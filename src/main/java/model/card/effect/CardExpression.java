package model.card.effect;

import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public abstract class CardExpression {

	public abstract void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain);

}
