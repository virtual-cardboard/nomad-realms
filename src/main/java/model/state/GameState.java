package model.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import model.GameObject;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.ItemActor;
import model.actor.NPCActor;
import model.actor.Structure;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.chain.ChainHeap;
import model.hidden.HiddenGameObject;
import model.task.Task;
import model.world.WorldMap;
import model.world.tile.Tile;

public class GameState {

	private Map<Long, WorldCard> cards = new HashMap<>();
	private Map<Long, Actor> actors = new HashMap<>();
	private Map<Long, Task> tasks = new HashMap<>();
	private Map<Long, HiddenGameObject> hiddens = new HashMap<>();

	private transient List<CardPlayer> cardPlayers = new ArrayList<>();
	private transient List<NPCActor> npcs = new ArrayList<>();
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
			corresponding = (T) worldMap().chunk(tile.longID()).tile(Tile.tileCoords(tile.longID()));
		} else {
			GameObject actor = object;
			corresponding = (T) actor(actor.longID());
		}
		return corresponding;
	}

	public Map<Long, HiddenGameObject> hiddens() {
		return hiddens;
	}

	public WorldMap worldMap() {
		return worldMap;
	}

	/**
	 * Should not be called by {@link GameObject#addTo(GameState)}
	 * 
	 * @param object
	 */
	public void add(GameObject object) {
		object.addTo(this);
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

	public List<NPCActor> npcs() {
		return npcs;
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

	public ItemActor item(Long id) {
		return (ItemActor) actors.get(id);
	}

	public Task task(long id) {
		return tasks.get(id);
	}

	public List<Actor> actors(Vector2i key) {
		return chunkToActors().get(key);
	}

	public List<Actor> getActorsAroundChunk(Vector2i chunkPos) {
		List<Actor> actors = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				List<Actor> a = actors(chunkPos.add(j, i));
				if (a != null) {
					actors.addAll(a);
				}
			}
		}
		return actors;
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
		tasks.forEach((Long id, Task task) -> {
			copy.add(task.copy());
		});
		cardPlayers.forEach((CardPlayer player) -> {
			CardDashboard dashboard = player.cardDashboard().copy();
			copy.getCorresponding(player).setCardDashboard(dashboard);
			dashboard.hand().forEach(copy::add);
			dashboard.deck().forEach(copy::add);
			dashboard.discard().forEach(copy::add);
			dashboard.queue().forEach(cardPlayedEvent -> copy.add(cardPlayedEvent.cardID().getFrom(this)));
			if (player.cardDashboard().task() != null) {
				dashboard.setTask(player.cardDashboard().task().id().getFrom(copy));
			}
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

	public Map<Long, Task> tasks() {
		return tasks;
	}

}
