package model.card.effect;

import event.game.logicprocessing.expression.DrawCardEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public class DrawCardExpression extends CardExpression {

	private int amount;

	public DrawCardExpression() {
		this(1);
	}

	public DrawCardExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		chain.add(new DrawCardEvent(playedBy, target != null ? (CardPlayer) target : playedBy, amount));
	}

}
