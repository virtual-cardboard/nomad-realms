package model;

import static model.card.CardRarity.ARCANE;
import static model.card.CardRarity.BASIC;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.effect.CardTargetType.CHARACTER;
import static model.card.effect.CardTargetType.TILE;
import static model.tile.TileType.GRASS;
import static model.tile.TileType.SAND;
import static model.tile.TileType.WATER;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import event.game.logicprocessing.expression.RegenesisExpression;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.HealthActor;
import model.actor.Nomad;
import model.actor.PositionalActor;
import model.card.CardDashboard;
import model.card.GameCard;
import model.card.effect.CardEffect;
import model.card.effect.DealDamageExpression;
import model.card.effect.SelfDrawCardExpression;
import model.card.effect.TeleportExpression;
import model.chain.ChainHeap;
import model.tile.Tile;
import model.tile.TileChunk;
import model.tile.TileMap;
import model.tile.TileType;

public class GameState {

	private TileMap tileMap;
	private Map<Long, Actor> actors = new HashMap<>();
	private Map<Long, GameCard> cards = new HashMap<>();
	private Map<Long, CardPlayer> cardPlayers = new HashMap<>();
	private Map<CardPlayer, CardDashboard> dashboards = new HashMap<>();
	private Map<Vector2i, List<PositionalActor>> chunkToActors = new HashMap<>();
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
		n1.pos().add(300, 500);
		Nomad n2 = new Nomad();
		n2.pos().add(600, 300);
		add(n1);
		add(n2);
		dashboards.put(n1, new CardDashboard());
		dashboards.put(n2, new CardDashboard());
		fillDeck(n1);
		fillDeck(n2);
	}

	private void fillDeck(Nomad n) {
		GameCard extraPrep = new GameCard("Extra preparation", ACTION, BASIC, new CardEffect(null, a -> true, new SelfDrawCardExpression(2)), 3, "Draw 2.");
		GameCard meteor = new GameCard("Meteor", ACTION, BASIC, new CardEffect(TILE, a -> true, new SelfDrawCardExpression()), 1,
				"Deal 8 to all characters within radius 3 of target tile.");
		GameCard zap = new GameCard("Zap", CANTRIP, BASIC, new CardEffect(CHARACTER, a -> a instanceof HealthActor, new DealDamageExpression(3)), 0, "Deal 3.");
		GameCard teleport = new GameCard("Teleport", CANTRIP, ARCANE, new CardEffect(TILE, a -> true, new TeleportExpression()), 0,
				"Teleport to target tile within radius 4.");
		CardDashboard dashboard = dashboard(n);
		add(extraPrep);
		add(meteor);
		add(zap);
		add(teleport);
		dashboard.hand().addTop(meteor);
		dashboard.hand().addTop(extraPrep);
		dashboard.hand().addTop(zap);
		dashboard.hand().addTop(teleport);
		for (int i = 0; i < 8; i++) {
			GameCard zapCopy = zap.copyDiffID();
			dashboard.deck().addTop(zapCopy);
			add(zapCopy);
		}
		for (int i = 0; i < 5; i++) {
			GameCard teleportCopy = teleport.copyDiffID();
			dashboard.deck().addTop(teleportCopy);
			add(teleportCopy);
		}
		for (int i = 0; i < 8; i++) {
			GameCard extraPrepCopy = extraPrep.copyDiffID();
			dashboard.deck().addTop(extraPrepCopy);
			add(extraPrepCopy);
		}
		dashboard.deck().shuffle(0);
		GameCard regenesis = new GameCard("Regenesis", ACTION, BASIC, new CardEffect(null, a -> true, new RegenesisExpression()), 15,
				"When this card enters discard from anywhere, shuffle discard into deck.");
		add(regenesis);
		dashboard.deck().addBottom(regenesis);
	}

	@SuppressWarnings("unchecked")
	public <T extends Actor> T getCorresponding(T object) {
		T corresponding = null;
		if (object instanceof Tile) {
			Tile tile = (Tile) object;
			corresponding = (T) tileMap.chunk(tile.id()).tile(tile.x(), tile.y());
		} else {
			Actor actor = object;
			corresponding = (T) actor(actor.id());
		}
		return corresponding;
	}

	public TileMap tileMap() {
		return tileMap;
	}

	public CardDashboard dashboard(CardPlayer cardPlayer) {
		return dashboards.get(cardPlayer);
	}

	public Collection<CardDashboard> dashboards() {
		return dashboards.values();
	}

	public void add(Actor actor) {
		actor.addTo(actors, cardPlayers, cards, chunkToActors);
	}

	public Actor actor(Long id) {
		return actors.get(id);
	}

	public GameCard card(long cardID) {
		return cards.get(cardID);
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

	public List<PositionalActor> actors(Vector2i key) {
		return chunkToActors.get(key);
	}

	public ChainHeap chainHeap() {
		return chainHeap;
	}

}
