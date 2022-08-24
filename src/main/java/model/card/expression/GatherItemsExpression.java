package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.GatherItemsEvent;
import model.id.CardPlayerId;
import model.id.Id;
import model.state.GameState;

public class GatherItemsExpression extends CardExpression {

	private int radius;

	public GatherItemsExpression(int radius) {
		this.radius = radius;
	}

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new GatherItemsEvent(playerID, radius));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(CardTag.GATHERS_ITEMS);
	}

	@Override
	public String toString() {
		return "(Gather " + radius + ")";
	}

}
