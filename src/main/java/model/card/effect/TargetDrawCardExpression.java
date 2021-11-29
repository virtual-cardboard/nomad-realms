package model.card.effect;

import event.game.logicprocessing.expression.DrawCardEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;
import model.chain.EffectChain;

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
