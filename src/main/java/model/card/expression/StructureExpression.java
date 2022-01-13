package model.card.expression;

import event.game.logicprocessing.chain.SpawnStructureEvent;
import model.chain.EffectChain;
import model.state.GameState;
import model.structure.StructureType;

public class StructureExpression extends CardExpression {

	private StructureType type;

	public StructureExpression(StructureType type) {
		this.type = type;
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.add(new SpawnStructureEvent(playerID, targetID, type));
	}

}
