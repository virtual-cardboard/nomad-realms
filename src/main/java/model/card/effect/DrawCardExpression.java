package model.card.effect;

import java.util.Queue;

import common.event.GameEvent;
import event.game.expression.DrawCardEvent;
import model.GameState;
import model.actor.Actor;
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
	public void process(CardPlayer playedBy, Actor target, GameState state, Queue<GameEvent> events) {
		events.add(new DrawCardEvent(playedBy, playedBy, amount));
	}

}
