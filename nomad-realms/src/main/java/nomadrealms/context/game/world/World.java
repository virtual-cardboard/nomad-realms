package nomadrealms.context.game.world;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.chunkCoordinateOf;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Collections.singletonList;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintPair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.card.effect.DropItemEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.RestockEffect;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
import nomadrealms.context.game.card.query.actor.StaticTargetQuery;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.event.InteractEvent;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Region;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.particle.context.game.CardParticle;
import nomadrealms.render.particle.spawner.BasicParticleSpawner;

/**
 * The world is the container for the map (to do: replace map with an object), along with the {@link Actor Actors} and
 * {@link Structure}s that inhabit it.
 */
public class World {

	private transient GameState state;

	private GameMap map;
	public Nomad nomad;

	public List<ProcChain> procChains = new ArrayList<>();

	private World() {
		state = null;
	}

	public World(GameState state, MapGenerationStrategy mapGenerationStrategy) {
		this.state = state;
		map = new GameMap(this, mapGenerationStrategy);
		mapGenerationStrategy.initializeMap(this);
	}

	public List<Chunk> getVisibleChunks(RenderingEnvironment re) {
		Vector2f minWorld = re.camera.position().vector();
		ChunkCoordinate minChunk = chunkCoordinateOf(minWorld).left().up();

		float chunkWidth = TILE_HORIZONTAL_SPACING * CHUNK_SIZE;
		float chunkHeight = TILE_VERTICAL_SPACING * CHUNK_SIZE;

		int numChunksX = (int) Math.ceil(re.config.getWidth() * 0.6f / (chunkWidth * re.camera.zoom().get())) + 2;
		int numChunksY = (int) Math.ceil(re.config.getHeight() / (chunkHeight * re.camera.zoom().get())) + 2;

		List<Chunk> visibleChunks = new ArrayList<>();
		ChunkCoordinate rowStart = minChunk;
		for (int y = 0; y < numChunksY; y++) {
			ChunkCoordinate current = rowStart;
			for (int x = 0; x < numChunksX; x++) {
				visibleChunks.add(getChunk(current));
				current = current.right();
			}
			rowStart = rowStart.down();
		}
		return visibleChunks;
	}

	public void renderMap(RenderingEnvironment re) {
		List<Chunk> visibleChunks = getVisibleChunks(re);
		for (Chunk chunk : visibleChunks) {
			chunk.render(re);
		}
		if (re.showDebugInfo) {
			Set<Zone> visibleZones = new HashSet<>();
			for (Chunk chunk : visibleChunks) {
				visibleZones.add(chunk.zone());
			}
			for (Zone zone : visibleZones) {
				zone.renderDebug(re);
			}
		}
	}

	public void spawnDeathParticles(Actor actor) {
		Tile deathTile = actor.tile();
		particlePool().addParticles(new SpawnParticlesEffect(
				actor,
				new BasicParticleSpawner(new StaticTargetQuery<>(deathTile), "pill")
						.particleCount(8)
						.size((i, s, t) -> new ConstraintPair(absolute(5), absolute(11)))
						.rotation((i, s, t) -> absolute((float) (i * 2 * PI / 8)))
						.position((i, s, t) -> {
							float angle = (float) (i * 2 * PI / 8);
							return new ConstraintPair(t.tile().pos().vector())
									.add(time().multiply((float) sin(angle)).multiply(0.1f),
											time().multiply((float) cos(angle)).multiply(-0.1f));
						})
						.lifetime((i, s, t) -> 500L),
				new EffectContext().source(actor).target(actor)
		));
		particlePool().addParticles(new SpawnParticlesEffect(
				actor,
				new BasicParticleSpawner(new StaticTargetQuery<>(deathTile), "text_pop")
						.size((i, s, t) -> new ConstraintPair(absolute(10), absolute(10)))
						.position((i, s, t) -> {
							float v0x = 0.05f;
							float v0y = -0.1f;
							return new ConstraintPair(t.tile().pos().vector())
									.add(time().multiply(v0x).add(time().multiply(time()).multiply(-v0x / 2000f)),
											time().multiply(v0y).add(time().multiply(time()).multiply(-v0y / 2000f)));
						})
						.lifetime((i, s, t) -> 500L),
				new EffectContext().source(actor).target(actor)
		));
	}

	public void renderActors(RenderingEnvironment re) {
		if (re.camera.zoom().get() < 0.25) {
			return;
		}
		List<Chunk> chunksToRender = getVisibleChunks(re);
		for (Chunk chunk : chunksToRender) {
			for (Tile tile : chunk.tiles()) {
				// TODO: eventually remove destroyed entities after a delay. not here, but in update()
				if (tile.actor() != null && !tile.actor().isDestroyed()) {
					tile.actor().render(re);
				}
			}
		}
	}

	public void update(InputEventFrame inputEventFrame) {
		Set<Actor> actorsToUpdate = new HashSet<>();
		if (nomad != null && nomad.tile() != null) {
			List<Chunk> surroundingChunks = nomad.tile().chunk().getSurroundingChunks();
			for (Chunk chunk : surroundingChunks) {
				if (chunk != null) {
					actorsToUpdate.addAll(chunk.actors());
				}
			}
		}
		for (Actor actor : actorsToUpdate) {
			if (actor.isDestroyed()) {
				if (actor.tile() != null) {
					spawnDeathParticles(actor);
					actor.tile().clearActor();
				}
				continue;
			}
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
		throw new IllegalStateException("You should override the double visitor pattern resolve method in "
				+ event.getClass().getSimpleName() + " and add a resolve method in World.");
	}

	public void resolve(CardPlayedEvent event) {
		Deck deck = event.card().deck();
		deck.removeCard(event.card());
		event.source().mana(event.source().mana() - event.card().card().manaCost());
		event.source().cardStack().add(event);
		if (!event.card().ephemeral()) {
			event.source().deckCollection().discardZone().addCard(event.card());
		}
		if (deck.size() == 0 && event.source().shouldRestock()) {
			procChains.add(new ProcChain(singletonList(new RestockEffect(event.source(), deck))));
		}
		particlePool().addParticle(new CardParticle(event));
		state.uiEventChannel.add(event);
	}

	public void resolve(DropItemEvent event) {
		Effect effect = new DropItemEffect((Actor) event.source(), event.source(), event.item(), event.tile());
		procChains.add(new ProcChain(singletonList(effect)));
		state.uiEventChannel.add(event);
	}

	public void resolve(InteractEvent event) {
		if (event.source().tile().coord().distanceTo(event.target().tile().coord()) <= event.target().interactRange()) {
			event.target().interact(event.source());
			state.uiEventChannel.add(event);
		}
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
		addActor(actor, false);
	}

	public void addActor(Actor actor, boolean forced) {
		if (state != null) {
			actor.particlePool(state.particlePool);
		}
		if (forced) {
			actor.tile().clearActor();
		}
		// TODO: figure out to do when tile is already occupied
		actor.tile().actor(actor);
	}

	public GameMap map() {
		return map;
	}

	/**
	 * Get the region at the given coordinate.
	 *
	 * @param coord the coordinate of the region to get
	 * @return the region at the given coordinate
	 */
	public Region getRegion(RegionCoordinate coord) {
		return map.getRegion(coord);
	}

	/**
	 * Get the zone at the given coordinate. Be careful, this method could be slow if the zone does not exist.
	 *
	 * @param coord the coordinate of the zone to get
	 * @return the zone at the given coordinate
	 */
	public Zone getZone(ZoneCoordinate coord) {
		return getRegion(coord.region()).lazyGetZone(coord);
	}

	/**
	 * Get the chunk at the given coordinate. Be careful, this method could be slow if the chunk does not exist.
	 *
	 * @param coord the coordinate of the chunk to get
	 * @return the chunk at the given coordinate
	 */
	public Chunk getChunk(ChunkCoordinate coord) {
		return getRegion(coord.region()).getChunk(coord);
	}

	/**
	 * Get the tile at the given coordinate. Be careful, this method could be slow if the tile does not exist.
	 *
	 * @param tile the coordinate of the tile to get
	 * @return the tile at the given coordinate
	 */
	public Tile getTile(TileCoordinate tile) {
		return getRegion(tile.region()).getTile(tile);
	}

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	public void reindex(GameState gameState) {
		this.state = gameState;
		map.reindex(this);
		if (nomad != null) {
			nomad.reindex(this);
		}
	}

	public MapGenerationStrategy generation() {
		return map.generation();
	}

	public GameState state() {
		return state;
	}

	public ParticlePool particlePool() {
		return state.particlePool;
	}

	public void particlePool(ParticlePool particlePool) {
		for (Region region : map.regions()) {
			for (Zone[] zoneRow : region.zones()) {
				for (Zone zone : zoneRow) {
					if (zone == null) continue;
					for (Chunk[] chunkRow : zone.chunks()) {
						for (Chunk chunk : chunkRow) {
							if (chunk == null) continue;
							for (Actor actor : chunk.actors()) {
								actor.particlePool(particlePool);
							}
						}
					}
				}
			}
		}
	}
}
