package model.card.effect;

import java.util.Queue;

import event.game.expression.ChainEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;

public class DoNothingExpression extends CardExpression {

	@Override
	public void process(CardPlayer playedBy, GameObject target, GameState state, Queue<ChainEvent> events) {
	}

}
