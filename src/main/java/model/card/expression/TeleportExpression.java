package model.card.expression;

import static model.card.CardTag.MOVEMENT;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.TeleportEvent;
import model.id.CardPlayerId;
import model.id.Id;
import model.id.TileId;
import model.state.GameState;

public class TeleportExpression extends CardExpression {

	public TeleportExpression() {
	}

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new TeleportEvent(playerID, targetId.as(TileId.class)));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(MOVEMENT);
	}

	@Override
	public String toString() {
		return "(Teleport)";
	}

}
