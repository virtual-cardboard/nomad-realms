package model.card.expression;

import event.game.logicprocessing.chain.DestroyEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class DestroyExpression extends CardExpression {

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.add(new DestroyEvent(playerID, targetID));
	}

}
