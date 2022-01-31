package model.card.expression;

import model.card.chain.SpawnStructureEvent;
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
		chain.addWheneverEvent(new SpawnStructureEvent(playerID, targetID, type));
	}

}
