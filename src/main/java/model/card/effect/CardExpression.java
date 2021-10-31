package model.card.effect;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.Actor;
import model.actor.CardPlayer;

public abstract class CardExpression {

	public abstract void process(CardPlayer playedBy, Actor target, GameState state, Queue<GameEvent> events);

}
