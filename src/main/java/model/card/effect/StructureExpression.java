package model.card.effect;

import java.util.function.Supplier;

import event.game.logicprocessing.expression.SpawnStructureEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.actor.Structure;
import model.chain.EffectChain;
import model.tile.Tile;

public class StructureExpression extends CardExpression {

	private Supplier<Structure> structureSupplier;

	public StructureExpression(Supplier<Structure> structureSupplier) {
		this.structureSupplier = structureSupplier;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		chain.add(new SpawnStructureEvent(playedBy, (Tile) target, structureSupplier.get()));
	}

}
