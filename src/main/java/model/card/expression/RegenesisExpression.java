package model.card.expression;

import event.game.logicprocessing.chain.RegenesisEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class RegenesisExpression extends CardExpression {

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.add(new RegenesisEvent(playerID));
	}

}
