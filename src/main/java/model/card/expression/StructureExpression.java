package model.card.expression;

import java.util.function.Supplier;

import event.game.logicprocessing.chain.SpawnStructureEvent;
import model.actor.Structure;
import model.chain.EffectChain;
import model.state.GameState;

public class StructureExpression extends CardExpression {

	private Supplier<Structure> structureSupplier;

	public StructureExpression(Supplier<Structure> structureSupplier) {
		this.structureSupplier = structureSupplier;
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.add(new SpawnStructureEvent(playerID, targetID, structureSupplier.get()));
	}

}
