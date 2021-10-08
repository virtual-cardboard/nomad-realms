package model.card.effect;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;

public abstract class CardEffect {

	public abstract void process(CardPlayer playedBy, GameState state, Queue<GameEvent> events);

}
