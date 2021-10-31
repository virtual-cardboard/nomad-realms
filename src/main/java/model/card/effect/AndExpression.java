package model.card.effect;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.Actor;
import model.actor.CardPlayer;

public class AndExpression extends CardExpression {

	private CardExpression cardEffect1;
	private CardExpression cardEffect2;

	public AndExpression(CardExpression cardEffect1, CardExpression cardEffect2) {
		this.cardEffect1 = cardEffect1;
		this.cardEffect2 = cardEffect2;
	}

	@Override
	public void process(CardPlayer playedBy, Actor target, GameState state, Queue<GameEvent> events) {
		cardEffect1.process(playedBy, target, state, events);
		cardEffect2.process(playedBy, target, state, events);
	}

}
