package model;

import java.util.Map;

import model.actor.Actor;
import model.actor.CardPlayer;
import model.card.CardHand;
import model.map.TileMap;

public class GameState {

	private TileMap tileMap;
	private Map<Long, Actor> actors;

	private Map<CardPlayer, CardHand> cardHands;

	public TileMap tileMap() {
		return tileMap;
	}

	public CardHand cardHand(CardPlayer player) {
		return cardHands.get(player);
	}

	public Actor actor(Long id) {
		return actors.get(id);
	}

	public void cardDeck(CardPlayer target) {

	}

}
