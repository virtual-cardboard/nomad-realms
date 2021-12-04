package model.card.effect;

import event.game.logicprocessing.expression.RegenesisEvent;
import model.GameState;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public class RegenesisExpression extends CardExpression {

	@Override
	public void handle(CardPlayer playedBy, Actor target, GameState state, EffectChain chain) {
		chain.add(new RegenesisEvent(playedBy));
	}

}
