package model.card.expression;

import event.game.logicprocessing.expression.DrawCardEvent;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;
import model.state.GameState;

public class SelfDrawCardExpression extends CardExpression {

	private int amount;

	public SelfDrawCardExpression() {
		this(1);
	}

	public SelfDrawCardExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		chain.add(new DrawCardEvent(playedBy, playedBy, amount));
	}

}
