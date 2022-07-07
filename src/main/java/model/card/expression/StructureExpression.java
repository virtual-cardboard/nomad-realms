package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.SpawnStructureEvent;
import model.id.CardPlayerId;
import model.id.Id;
import model.id.TileId;
import model.state.GameState;
import model.structure.StructureType;

public class StructureExpression extends CardExpression {

	private StructureType type;

	public StructureExpression(StructureType type) {
		this.type = type;
	}

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new SpawnStructureEvent(playerID, targetId.as(TileId.class), type));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
	}

}
