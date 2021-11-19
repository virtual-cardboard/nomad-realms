package model;

import static model.tile.TileType.GRASS;
import static model.tile.TileType.SAND;
import static model.tile.TileType.WATER;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import common.math.Vector2i;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.Nomad;
import model.card.CardDashboard;
import model.chain.ChainHeap;
import model.tile.TileChunk;
import model.tile.TileMap;
import model.tile.TileType;

public class GameState {

	private TileMap tileMap;
	private Map<Long, Actor> actors = new HashMap<>();
	private Map<Long, CardPlayer> cardPlayers = new HashMap<>();
	private Map<CardPlayer, CardDashboard> dashboards = new HashMap<>();
	private ChainHeap chainHeap = new ChainHeap();

	public GameState() {
		TileType[][] chunk1 = {
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, SAND, SAND, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, WATER, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, GRASS, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, WATER, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, SAND, SAND, SAND, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, GRASS, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, WATER, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, SAND, SAND, SAND, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, GRASS, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ SAND, WATER, WATER, WATER, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, SAND, SAND, SAND, SAND, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
				{ GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS }
		};
		tileMap = new TileMap();
		tileMap.addChunk(new TileChunk(new Vector2i(), chunk1));
		tileMap.addChunk(new TileChunk(new Vector2i(1, 0), chunk1));
		tileMap.addChunk(new TileChunk(new Vector2i(0, 1), chunk1));
		tileMap.addChunk(new TileChunk(new Vector2i(1, 1), chunk1));
		Nomad n1 = new Nomad();
		n1.pos().translate(800, 760);
		Nomad n2 = new Nomad();
		n2.pos().translate(900, 600);
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

	public ChainHeap chainHeap() {
		return chainHeap;
	}

}
