package model.state;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.card.GameCard;
import model.chain.ChainHeap;
import model.tile.Tile;
import model.tile.WorldMap;

public class GameState {

	private Map<Long, GameCard> cards = new HashMap<>();
	private Map<Long, Actor> actors = new HashMap<>();

	private transient Map<Long, CardPlayer> cardPlayers = new HashMap<>();
	private transient Map<Vector2i, List<Actor>> chunkToActors = new HashMap<>();

	private WorldMap worldMap = new WorldMap();
	private ChainHeap chainHeap = new ChainHeap();

	@SuppressWarnings("unchecked")
	public <T extends GameObject> T getCorresponding(T object) {
		T corresponding = null;
		if (object instanceof Tile) {
			Tile tile = (Tile) object;
			corresponding = (T) worldMap.chunk(tile.id()).tile(tile.x(), tile.y());
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
		actor.addTo(actors, cardPlayers, cards, chunkToActors);
	}

	public GameCard card(long cardID) {
		return cards.get(cardID);
	}

	public Actor actor(long id) {
		return actors.get(id);
	}

	public Collection<Actor> actors() {
		return actors.values();
	}

	public Collection<CardPlayer> cardPlayers() {
		return cardPlayers.values();
	}

	public CardPlayer cardPlayer(Long id) {
		return cardPlayers.get(id);
	}

	public List<Actor> actors(Vector2i key) {
		return chunkToActors.get(key);
	}

	public ChainHeap chainHeap() {
		return chainHeap;
	}

	public GameState copy() {
		GameState copy = new GameState();
		cards.forEach((Long id, GameCard card) -> {
			copy.cards.put(id, card.copy());
		});
		actors.forEach((Long id, Actor actor) -> {
			copy.add(actor.copy());
		});
		copy.worldMap = worldMap.copy();
		copy.chainHeap = chainHeap.copy();
		return copy;
	}

}
