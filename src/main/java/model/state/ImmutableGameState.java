package model.state;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.card.GameCard;
import model.chain.ChainHeap;
import model.tile.WorldMap;

public class ImmutableGameState {

	private WorldMap worldMap = new WorldMap();
	private Map<Long, GameCard> cards = new HashMap<>();
	private Map<Long, Actor> actors = new HashMap<>();
	private ChainHeap chainHeap = new ChainHeap();

	private transient Map<Long, CardPlayer> cardPlayers = new HashMap<>();
	private transient Map<Vector2i, List<Actor>> chunkToActors = new HashMap<>();

	public ImmutableGameState(WorldMap worldMap, Map<Long, GameCard> cards, Map<Long, Actor> actors, ChainHeap chainHeap, Map<Long, CardPlayer> cardPlayers,
			Map<Vector2i, List<Actor>> chunkToActors) {
		this.worldMap = worldMap;
		this.cards = Collections.unmodifiableMap(new HashMap<>(cards));
		this.actors = Collections.unmodifiableMap(new HashMap<>(actors));
		this.chainHeap = chainHeap;
		this.cardPlayers = Collections.unmodifiableMap(new HashMap<>(cardPlayers));
		this.chunkToActors = Collections.unmodifiableMap(new HashMap<>(chunkToActors));
	}

	public WorldMap worldMap() {
		return worldMap;
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

}
