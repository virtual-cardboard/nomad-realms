package model.card.effect;

import java.util.Queue;

import event.game.expression.CardExpressionEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;

public abstract class CardExpression {

	public abstract void process(CardPlayer playedBy, GameObject target, GameState state, Queue<CardExpressionEvent> events);

}
