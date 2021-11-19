package model.card.effect;

import java.util.Queue;

import event.game.logicprocessing.chain.ChainEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.HealthActor;

public class DealDamageExpression extends CardExpression {

	private int damage;

	public DealDamageExpression(int damage) {
		this.damage = damage;
	}

	@Override
	public void process(CardPlayer playedBy, GameObject target, GameState state, Queue<ChainEvent> chain) {
		HealthActor actor = (HealthActor) target;
		actor.changeHealth(-damage);
	}

}
