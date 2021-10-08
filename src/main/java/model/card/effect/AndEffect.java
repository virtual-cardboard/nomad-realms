package model.card.effect;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.Actor;
import model.actor.CardPlayer;

public class AndEffect extends CardEffect {

	private CardEffect cardEffect1;
	private CardEffect cardEffect2;

	public AndEffect(CardEffect cardEffect1, CardEffect cardEffect2) {
		this.cardEffect1 = cardEffect1;
		this.cardEffect2 = cardEffect2;
	}

	@Override
	public void process(CardPlayer playedBy, Actor target, GameState state, Queue<GameEvent> events) {
		cardEffect1.process(playedBy, target, state, events);
		cardEffect2.process(playedBy, target, state, events);
	}

}
