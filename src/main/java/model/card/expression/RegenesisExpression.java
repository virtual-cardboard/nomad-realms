package model.card.expression;

import event.game.logicprocessing.expression.RegenesisEvent;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;
import model.state.GameState;

public class RegenesisExpression extends CardExpression {

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		chain.add(new RegenesisEvent(playedBy));
	}

}
