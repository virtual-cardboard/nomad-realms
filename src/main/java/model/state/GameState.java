package model.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.actor.Structure;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.chain.ChainHeap;
import model.world.Tile;
import model.world.WorldMap;

public class GameState {

	private Map<Long, WorldCard> cards = new HashMap<>();
	private Map<Long, Actor> actors = new HashMap<>();

	private transient List<CardPlayer> cardPlayers = new ArrayList<>();
	private transient List<Structure> structures = new ArrayList<>();
	private transient Map<Vector2i, List<Actor>> chunkToActors = new HashMap<>();
	private transient Map<Vector2i, List<Structure>> chunkToStructures = new HashMap<>();

	private WorldMap worldMap = new WorldMap();
	private ChainHeap chainHeap = new ChainHeap();

	@SuppressWarnings("unchecked")
	public <T extends GameObject> T getCorresponding(T object) {
		T corresponding = null;
		if (object instanceof Tile) {
			Tile tile = (Tile) object;
			corresponding = (T) worldMap.finalLayerChunk(tile.id()).tile(tile.x(), tile.y());
		} else {
			GameObject actor = object;
			corresponding = (T) actor(actor.id());
		}
		return corresponding;
	}

	public WorldMap worldMap() {
		return worldMap;
	}

	public void add(GameObject actor) {
		actor.addTo(this);
	}

	public WorldCard card(long cardID) {
		return cards().get(cardID);
	}

	public Actor actor(long id) {
		return actors.get(id);
	}

	public Map<Long, Actor> actors() {
		return actors;
	}

	public List<CardPlayer> cardPlayers() {
		return cardPlayers;
	}

	public List<Structure> structures() {
		return structures;
	}

	public CardPlayer cardPlayer(Long id) {
		return (CardPlayer) actors.get(id);
	}

	public Structure structure(Long id) {
		return (Structure) actors.get(id);
	}

	public List<Actor> actors(Vector2i key) {
		return chunkToActors().get(key);
	}

	public List<Structure> structures(Vector2i key) {
		return chunkToStructures().get(key);
	}

	public ChainHeap chainHeap() {
		return chainHeap;
	}

	public GameState copy() {
		GameState copy = new GameState();
		actors.forEach((Long id, Actor actor) -> {
			copy.add(actor.copy());
		});
		cardPlayers.forEach((CardPlayer player) -> {
			CardDashboard dashboard = player.cardDashboard().copy();
			copy.getCorresponding(player).setCardDashboard(dashboard);
			dashboard.hand().forEach(copy::add);
			dashboard.deck().forEach(copy::add);
			dashboard.discard().forEach(copy::add);
			dashboard.queue().forEach(cardPlayedEvent -> copy.add(card(cardPlayedEvent.cardID())));
		});
		copy.worldMap = worldMap.copy();
		copy.chainHeap = chainHeap.copy();
		return copy;
	}

	public Map<Vector2i, List<Actor>> chunkToActors() {
		return chunkToActors;
	}

	public Map<Vector2i, List<Structure>> chunkToStructures() {
		return chunkToStructures;
	}

	public Map<Long, WorldCard> cards() {
		return cards;
	}

}
