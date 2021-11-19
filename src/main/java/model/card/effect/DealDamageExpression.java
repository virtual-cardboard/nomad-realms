package model.card.effect;

import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.HealthActor;
import model.chain.EffectChain;

public class DealDamageExpression extends CardExpression {

	private int damage;

	public DealDamageExpression(int damage) {
		this.damage = damage;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		HealthActor actor = (HealthActor) target;
		actor.changeHealth(-damage);
	}

}
