package model.card.effect;

import event.game.logicprocessing.expression.TakeDamageEvent;
import model.GameState;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.HealthActor;
import model.chain.EffectChain;

public class DealDamageExpression extends CardExpression {

	private int num;

	public DealDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(CardPlayer playedBy, Actor target, GameState state, EffectChain chain) {
		chain.add(new TakeDamageEvent(playedBy, (HealthActor) target, num));
	}

}
