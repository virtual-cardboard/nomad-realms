package nomadrealms.game.world;

import static java.util.Collections.singletonList;
import static nomadrealms.game.item.Item.OAK_LOG;
import static nomadrealms.game.item.Item.WHEAT_SEED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nomadrealms.game.GameState;
import nomadrealms.game.actor.Actor;
import nomadrealms.game.actor.HasPosition;
import nomadrealms.game.actor.cardplayer.Farmer;
import nomadrealms.game.actor.cardplayer.FeralMonkey;
import nomadrealms.game.actor.cardplayer.Nomad;
import nomadrealms.game.actor.structure.Structure;
import nomadrealms.game.card.intent.DropItemIntent;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.DropItemEvent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.InputEventFrame;
import nomadrealms.game.event.ProcChain;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Region;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.game.world.map.generation.TemplateGenerationStrategy;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;

/**
 * The world is the container for the map (to do: replace map with an object), along with the {@link Actor Actors} and
 * {@link Structure}s that inhabit it.
 */
public class World {

	private final GameState state;

	private GameMap map;
	public Nomad nomad;
	public List<Actor> actors = new ArrayList<>();
	public List<Structure> structures = new ArrayList<>();
	public Map<Tile, HasPosition> tileToEntityMap;

	public List<ProcChain> procChains = new ArrayList<>();

	public MapGenerationStrategy mapGenerationStrategy = new TemplateGenerationStrategy();

	public World(GameState state) {
		this.state = state;
		map = new GameMap(this, mapGenerationStrategy);
		nomad = new Nomad("Donny", getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0),
				0, 8)));
		nomad.inventory().add(new WorldItem(OAK_LOG));
		nomad.inventory().add(new WorldItem(WHEAT_SEED));
		Farmer farmer = new Farmer("Fred", getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0),
				0, 1), 0, 0)));
		actors.add(nomad);
		actors.add(farmer);
		// Add a feral monkey
		actors.add(new FeralMonkey("bob", getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 6, 6))));
	}

	public void renderMap(RenderingEnvironment re) {
		map.render(re, re.camera.position());
	}

	public void renderActors(RenderingEnvironment re) {
		for (Actor entity : actors) {
			entity.render(re);
		}
	}

	int x = 0;
	int i = 0;

	public void update(InputEventFrame inputEventFrame) {
		i++;
		if (i % 10 == 0) {
			x = Math.min(x + 1, 15);
//			nomad.tile(nomad.tile().dr(this));
			i = 0;
		}
		tileToEntityMap = new HashMap<>();
		for (HasPosition entity : actors) {
			tileToEntityMap.put(entity.tile(), entity);
		}
		for (Actor actor : actors) {
			actor.update(this.state);
			for (InputEvent event : actor.retrieveNextPlays()) {
				event.resolve(this);
			}
		}
		procChains.removeIf(ProcChain::empty);
		for (ProcChain chain : new ArrayList<>(procChains)) {
			chain.update(this);
		}
	}

	public void resolve(InputEvent event) {
		System.out.println("You should override the double visitor pattern resolve method in "
				+ event.getClass().getSimpleName() + " and add a resolve method in World.");
	}

	public void resolve(CardPlayedEvent event) {
		Deck deck = (Deck) event.card().zone();
		deck.removeCard(event.card());
		procChains.add(event.procChain(this));
		deck.addCard(event.card());
		state.uiEventChannel.add(event);
	}

	public void resolve(DropItemEvent event) {
		Intent intent = new DropItemIntent(event.source(), event.item());
		procChains.add(new ProcChain(singletonList(intent)));
		state.uiEventChannel.add(event);
	}

	public HasPosition getTargetOnTile(Tile tile) {
		return tileToEntityMap.get(tile);
	}

	public void setTile(Tile tile) {
		tile.chunk().replace(tile);
	}

	public void addProcChain(ProcChain chain) {
		procChains.add(chain);
	}

	public void addAllProcChains(List<ProcChain> chains) {
		procChains.addAll(chains);
	}

	public void addActor(Actor actor) {
		actors.add(actor);
		if (actor instanceof Structure) {
			structures.add((Structure) actor);
		}
	}

	public Iterable<Region> regions() {
		return map.regions();
	}

	public Region getRegion(RegionCoordinate coord) {
		return map.getRegion(coord);
	}

	public Chunk getChunk(ChunkCoordinate chunkCoord) {
		return getRegion(chunkCoord.region()).getChunk(chunkCoord);
	}

	public Tile getTile(TileCoordinate tile) {
		return getRegion(tile.region()).getTile(tile);
	}

}
