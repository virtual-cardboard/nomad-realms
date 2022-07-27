package model.state;

import static model.GameObject.UNSET_ID;
import static model.ModelSerializationFormats.GAME_STATE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.format.Serializable;
import engine.common.math.Vector2i;
import model.GameObject;
import model.ModelSerializationFormats;
import model.actor.Actor;
import model.actor.ActorDeserializer;
import model.actor.CardPlayer;
import model.actor.ItemActor;
import model.actor.NpcActor;
import model.actor.Structure;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.chain.ChainHeap;
import model.hidden.HiddenGameObject;
import model.world.WorldMap;
import model.world.tile.Tile;

public class GameState implements Serializable {

	private Map<Long, WorldCard> cards = new HashMap<>();
	private Map<Long, Actor> actors = new HashMap<>();
	private Map<Long, HiddenGameObject> hiddens = new HashMap<>();

	private transient List<CardPlayer> cardPlayers = new ArrayList<>();
	private transient List<NpcActor> npcs = new ArrayList<>();
	private transient List<Structure> structures = new ArrayList<>();
	private transient Map<Vector2i, List<Actor>> chunkToActors = new HashMap<>();
	private transient Map<Vector2i, List<Structure>> chunkToStructures = new HashMap<>();

	private transient WorldMap worldMap = new WorldMap();
	private ChainHeap chainHeap = new ChainHeap();

	public GameState() {
	}

	public GameState(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

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
	 */
	public void add(GameObject object) {
		if (object.longID() == UNSET_ID) {
			throw new IllegalStateException("A " + object.getClass().getSimpleName() + " with an unset id cannot be added to a game state!\n" +
					"Maybe you forgot to generate its Id?");
		}
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

	public List<NpcActor> npcs() {
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
		cardPlayers.forEach((CardPlayer player) -> {
			CardDashboard dashboard = player.cardDashboard().copy();
			copy.getCorresponding(player).setCardDashboard(dashboard);
			dashboard.hand().forEach(copy::add);
			dashboard.deck().forEach(copy::add);
			dashboard.discard().forEach(copy::add);
			dashboard.queue().forEach(cardPlayedEvent -> copy.add(cardPlayedEvent.cardID().getFrom(this)));
			if (player.cardDashboard().task() != null) {
				dashboard.setTask(player.cardDashboard().task().copy());
			}
		});
		copy.worldMap = worldMap.copy();
		copy.chainHeap = chainHeap.copy();
		return copy;
	}

	@Override
	public ModelSerializationFormats formatEnum() {
		return GAME_STATE;
	}

	@Override
	public void read(SerializationReader reader) {
		for (byte i0 = 0, numElements0 = reader.readByte(); i0 < numElements0; i0++) {
			WorldCard pojo1 = new WorldCard();
			pojo1.read(reader);
			cards.put(pojo1.longID(), pojo1);
		}
		for (byte i0 = 0, numElements0 = reader.readByte(); i0 < numElements0; i0++) {
			Actor pojo1 = ActorDeserializer.deserialize(reader);
			actors.put(pojo1.longID(), pojo1);
		}
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume((byte) cards.size());
		cards.forEach((id, card) -> card.write(writer));
		writer.consume((byte) actors.size());
		actors.forEach((id, card) -> card.write(writer));
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
