package model;

import java.util.Map;

import model.actor.Actor;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.map.TileMap;

public class GameState {

	private TileMap tileMap;
	private Map<Long, Actor> actors;

	private Map<CardPlayer, CardDashboard> dashboards;

	public TileMap tileMap() {
		return tileMap;
	}

	public CardDashboard dashboard(CardPlayer player) {
		return dashboards.get(player);
	}

	public Actor actor(Long id) {
		return actors.get(id);
	}

}
