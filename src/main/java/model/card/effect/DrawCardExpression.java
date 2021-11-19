package model.card.effect;

import java.util.Queue;

import event.game.logicprocessing.chain.ChainEvent;
import event.game.logicprocessing.expression.DrawCardEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;

public class DrawCardExpression extends CardExpression {

	private int amount;

	public DrawCardExpression() {
		this(1);
	}

	public DrawCardExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public void process(CardPlayer playedBy, GameObject target, GameState state, Queue<ChainEvent> chain) {
		chain.add(new DrawCardEvent(playedBy, (CardPlayer) target, amount));
	}

}
