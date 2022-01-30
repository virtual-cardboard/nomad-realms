package model.card.expression;

import model.card.chain.GatherItemEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class GatherItemsExpression extends CardExpression {

	private int radius;

	public GatherItemsExpression(int radius) {
		this.radius = radius;
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.add(new GatherItemEvent(playerID, radius));
	}

}
