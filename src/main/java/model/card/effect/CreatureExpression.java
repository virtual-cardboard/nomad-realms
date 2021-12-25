package model.card.effect;

import java.util.function.Supplier;

import event.game.logicprocessing.expression.SpawnCreatureEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.Creature;
import model.actor.GameObject;
import model.chain.EffectChain;
import model.tile.Tile;

public class CreatureExpression extends CardExpression {

	private Supplier<Creature> creatureSupplier;

	public CreatureExpression(Supplier<Creature> creatureSupplier) {
		this.creatureSupplier = creatureSupplier;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		chain.add(new SpawnCreatureEvent(playedBy, (Tile) target, creatureSupplier.get()));
	}

}
