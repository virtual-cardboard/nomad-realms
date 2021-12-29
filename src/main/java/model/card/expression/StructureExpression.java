package model.card.expression;

import java.util.function.Supplier;

import event.game.logicprocessing.expression.SpawnStructureEvent;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.actor.Structure;
import model.chain.EffectChain;
import model.state.GameState;
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
