package model.card.expression;

import model.card.chain.TeleportEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class TeleportExpression extends CardExpression {

	public TeleportExpression() {
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new TeleportEvent(playerID, targetID));
	}

}
