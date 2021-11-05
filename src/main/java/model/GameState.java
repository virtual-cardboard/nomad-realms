package model;

import static model.map.tile.TileType.GRASS;
import static model.map.tile.TileType.SAND;
import static model.map.tile.TileType.WATER;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.Nomad;
import model.card.CardDashboard;
import model.map.TileMap;
import model.map.tile.TileType;

public class GameState {

	private TileMap tileMap;
	private Map<Long, Actor> actors = new HashMap<>();
	private Map<Long, CardPlayer> cardPlayers = new HashMap<>();
	private Map<CardPlayer, CardDashboard> dashboards = new HashMap<>();

	public GameState() {
		TileType[][] types = {
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, SAND, SAND, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, WATER, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, GRASS, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, WATER, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, SAND, SAND, SAND, SAND, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS }
		};
		tileMap = new TileMap(types);
		Nomad n1 = new Nomad();
		Nomad n2 = new Nomad();
		add(n1);
		add(n2);
		dashboards.put(n1, new CardDashboard());
		dashboards.put(n2, new CardDashboard());
	}

	public TileMap tileMap() {
		return tileMap;
	}

	public CardDashboard dashboard(CardPlayer cardPlayer) {
		return dashboards.get(cardPlayer);
	}

	public void add(Actor actor) {
		actor.addTo(actors, cardPlayers);
	}

	public Actor actor(Long id) {
		return actors.get(id);
	}

	public Collection<Actor> actors() {
		return actors.values();
	}

	public CardPlayer cardPlayer(Long id) {
		return cardPlayers.get(id);
	}

}
