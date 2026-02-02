package nomadrealms.context.game.world;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.chunkCoordinateOf;

import java.util.ArrayList;
import java.util.List;

import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.card.effect.DropItemEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InputEventFrame;
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
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.ParticleParameters;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.particle.geometry.RectangleParticle;
import nomadrealms.render.particle.spawner.ParticleSpawner;

/**
 * The world is the container for the map (to do: replace map with an object),
 * along with the {@link Actor Actors} and
 * {@link Structure}s that inhabit it.
 */
public class World {

	private transient GameState state;

	private GameMap map;
	public Nomad nomad;
	public List<Actor> actors = new ArrayList<>();
	public List<Structure> structures = new ArrayList<>();

	public List<ProcChain> procChains = new ArrayList<>();

	private World() {
		state = null;
	}

	public World(GameState state, MapGenerationStrategy mapGenerationStrategy) {
		this.state = state;
		map = new GameMap(this, mapGenerationStrategy);
		mapGenerationStrategy.initializeMap(this);
	}

	public void renderMap(RenderingEnvironment re) {
		map.render(re, re.camera.position().vector());

	}

	public void renderActors(RenderingEnvironment re) {
		ChunkCoordinate chunkCoord = chunkCoordinateOf(re.camera.position().vector());
		List<Chunk> chunksToRender = asList(
				getChunk(chunkCoord),
				getChunk(chunkCoord.up()),
				getChunk(chunkCoord.down()),
				getChunk(chunkCoord.left()),
				getChunk(chunkCoord.right()),
				getChunk(chunkCoord.down().right()),
				getChunk(chunkCoord.right().right()),
				getChunk(chunkCoord.down().down())
		);
		for (Chunk chunk : chunksToRender) {
			for (Tile tile : chunk.tiles()) {
				// TODO: eventually remove destroyed entities after a delay. not here, but in update()
				if (tile.actor() != null && !tile.actor().isDestroyed()) {
					tile.actor().render(re);
				}
			}
		}
	}

	int x = 0;
	int i = 0;

	public void update(InputEventFrame inputEventFrame) {
		// TODO: this actually does not update actors that werent added using addActor(), i.e. actors loaded from
		//  zone generation
		List<Actor> currentActors = new ArrayList<>(this.actors); // Prevent concurrent modification for added actors
		i++;
		if (i % 10 == 0) {
			x = Math.min(x + 1, 15);
			// nomad.tile(nomad.tile().dr(this));
			i = 0;
		}
		for (Actor actor : currentActors) {
			if (actor.isDestroyed()) {
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
		Deck deck = (Deck) event.card().zone();
		deck.removeCard(event.card());
		event.source().cardStack().add(event);
		if (!event.card().ephemeral()) {
			deck.addCard(event.card());
		}
		state.particlePool.addParticles(new SpawnParticlesEffect(
				new ParticleSpawner() {
					boolean spawned = false;

					@Override
					public boolean isComplete() {
						return spawned;
					}

					@Override
					public ParticleSpawner copy() {
						return this;
					}

					@Override
					public List<Particle> spawnParticles(ParticleParameters p) {
						spawned = true;
						RenderingEnvironment re = p.renderingEnvironment();
						return singletonList(new RectangleParticle(
								1000,
								new ConstraintBox(
										event.source().getScreenPosition(re).add(
												new ConstraintPair(
														absolute(0),
														time().multiply(time()).multiply(0.0004f).sub(time().multiply(0.4f))
												).scale(re.camera.zoom())
										),
										new ConstraintPair(absolute(10), absolute(15)).scale(re.camera.zoom())
								),
								time().multiply(0.002f),
								event.card().card().color()
						));
					}
				},
				new ParticleParameters().world(this).source(event.source())
		));
		state.uiEventChannel.add(event);
	}

	public void resolve(DropItemEvent event) {
		Effect effect = new DropItemEffect(event.source(), event.item(), event.tile());
		procChains.add(new ProcChain(singletonList(effect)));
		state.uiEventChannel.add(event);
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
		actors.add(actor);
		if (actor instanceof Structure) {
			structures.add((Structure) actor);
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
	 * Get the zone at the given coordinate. Be careful, this method could be slow
	 * if the zone does not exist.
	 *
	 * @param coord the coordinate of the zone to get
	 * @return the zone at the given coordinate
	 */
	public Zone getZone(ZoneCoordinate coord) {
		return getRegion(coord.region()).lazyGetZone(coord);
	}

	/**
	 * Get the chunk at the given coordinate. Be careful, this method could be slow
	 * if the chunk does not exist.
	 *
	 * @param coord the coordinate of the chunk to get
	 * @return the chunk at the given coordinate
	 */
	public Chunk getChunk(ChunkCoordinate coord) {
		return getRegion(coord.region()).getChunk(coord);
	}

	/**
	 * Get the tile at the given coordinate. Be careful, this method could be slow
	 * if the tile does not exist.
	 *
	 * @param tile the coordinate of the tile to get
	 * @return the tile at the given coordinate
	 */
	public Tile getTile(TileCoordinate tile) {
		return getRegion(tile.region()).getTile(tile);
	}

	public void reinitializeAfterLoad(GameState gameState) {
		this.state = gameState;
		map.reinitializeAfterLoad(this);
		nomad.reinitializeAfterLoad(this);
		for (Actor actor : actors) {
			if (actor instanceof CardPlayer) {
				((CardPlayer) actor).reinitializeAfterLoad(this);
			}
		}
		for (Structure structure : structures) {
			structure.reinitializeAfterLoad(this);
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
		for (Actor actor : actors) {
			actor.particlePool(particlePool);
		}
		for (Structure structure : structures) {
			structure.particlePool(particlePool);
		}
	}
}
