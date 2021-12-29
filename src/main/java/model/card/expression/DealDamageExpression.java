package model.card.expression;

import event.game.logicprocessing.expression.TakeDamageEvent;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.actor.HealthActor;
import model.chain.EffectChain;
import model.state.GameState;

public class DealDamageExpression extends CardExpression {

	private int num;

	public DealDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		chain.add(new TakeDamageEvent(playedBy, (HealthActor) target, num));
	}

}
