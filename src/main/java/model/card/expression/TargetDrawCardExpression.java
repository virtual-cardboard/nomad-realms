package model.card.expression;

import event.game.logicprocessing.expression.DrawCardEvent;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;
import model.state.GameState;

public class TargetDrawCardExpression extends CardExpression {

	private int amount;

	public TargetDrawCardExpression() {
		this(1);
	}

	public TargetDrawCardExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		chain.add(new DrawCardEvent(playedBy, (CardPlayer) target, amount));
	}

}
