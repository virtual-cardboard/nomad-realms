package model.card.expression;

import event.game.logicprocessing.chain.TeleportEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class TeleportExpression extends CardExpression {

	public TeleportExpression() {
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.add(new TeleportEvent(playerID, targetID));
	}

}
