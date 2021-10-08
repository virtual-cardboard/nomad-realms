package model;

import java.util.Map;

import model.actor.CardPlayer;
import model.card.CardHand;
import model.map.TileMap;

public class GameState {

	private TileMap tileMap;
	private Map<CardPlayer, CardHand> cardHands;

}
