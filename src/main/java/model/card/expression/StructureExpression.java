package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.card.chain.SpawnStructureEvent;
import model.chain.EffectChain;
import model.id.CardPlayerID;
import model.id.ID;
import model.id.TileID;
import model.state.GameState;
import model.structure.StructureType;

public class StructureExpression extends CardExpression {

	private StructureType type;

	public StructureExpression(StructureType type) {
		this.type = type;
	}

	@Override
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new SpawnStructureEvent(playerID, new TileID(targetID.toLongID()), type));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
	}

}
