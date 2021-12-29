package model.card.expression;

import event.game.logicprocessing.expression.RegenesisEvent;
import model.GameState;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public class RegenesisExpression extends CardExpression {

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		chain.add(new RegenesisEvent(playedBy));
	}

}
