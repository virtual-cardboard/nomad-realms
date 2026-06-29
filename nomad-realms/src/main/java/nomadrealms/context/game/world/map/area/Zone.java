package nomadrealms.context.game.world.map.area;

import static nomadrealms.context.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintPair;
import java.util.Random;

import engine.common.java.JavaUtil;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationLayer;
import nomadrealms.context.game.world.map.generation.overworld.GenerationProcess;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.points.PointsGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.points.point.PointOfInterest;
import nomadrealms.context.game.world.map.generation.overworld.structure.StructureGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.villager.VillagerGenerationStep;
import nomadrealms.render.RenderingEnvironment;

/**
 * A zone is a 16x16 grid of chunks. This is the optimal size for getting good layer-based map generation results, you
 * will see this a lot in the map generation code.
 * <br><br>
 * Some math:
 * <br>
 * Zone Dimensions = ({@link Tile#TILE_HORIZONTAL_SPACING TILE_X}, {@link Tile#TILE_VERTICAL_SPACING TILE_Y}) *
 * {@link ChunkCoordinate#CHUNK_SIZE CHUNK SIZE} = (30, 34.64) * 16 * 16 = (7680, 8867.84)
 * <br>
 */
public class Zone {

	private transient Region region;
	private final ZoneCoordinate coord;

	private Chunk[][] chunks;

	private GenerationProcess generationProcess;
	private GenerationLayer layer = GenerationLayer.NONE;

	private transient MapGenerationStrategy strategy;
	private transient World world;

	private transient Random rng;
	private int rngCounter = 0;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Zone() {
		this.region = null;
		this.coord = null;
		this.chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
	}


	/**
	 * Initializes the random number generator for this zone. This is necessary after deserialization, since the RNG
	 * cannot be serialized.
	 */
	public void initRNG(long worldSeed) {
		this.rng = new Random(coord.rngSeed(worldSeed));
		for (int i = 0; i < rngCounter; i++) {
			rng.nextInt();
		}
	}

	public Zone(World world, ZoneCoordinate coord, MapGenerationStrategy strategy) {
		this.world = world;
		this.region = world.getRegion(coord.region());
		this.coord = coord;
		this.strategy = strategy;
		this.chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
		initRNG(strategy.parameters().seed());
		generationProcess = new GenerationProcess(this, strategy);
		if (strategy instanceof nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy) {
			this.layer = GenerationLayer.NONE;
		} else {
			this.chunks = strategy.generateZone(world, this);
			this.layer = GenerationLayer.VILLAGER;
		}
	}

	public Zone advanceTo(GenerationLayer targetLayer) {
		if (layer.isAtLeast(targetLayer)) {
			return this;
		}
		if (targetLayer.previous() != null) {
			advanceTo(targetLayer.previous());
		}
		switch (targetLayer) {
			case BIOME:
				biomeGenerationStep().generate(world, strategy);
				break;
			case POINTS:
				pointsGenerationStep().generate(world, strategy);
				break;
			case STRUCTURE:
				initializeChunks(strategy);
				structureGenerationStep().generate(world, strategy);
				break;
			case VILLAGER:
				villagerGenerationStep().generate(world, strategy);
				break;
		}
		layer = targetLayer;
		return this;
	}

	private void initializeChunks(MapGenerationStrategy strategy) {
		for (ChunkCoordinate chunkCoord : JavaUtil.flatten(coord().chunkCoordinates())) {
			Chunk chunk = new Chunk(this, chunkCoord);
			chunk.tiles(strategy.generateChunk(this, chunk, chunkCoord));
			chunks[chunkCoord.x()][chunkCoord.y()] = chunk;
		}
		if (world.state() != null) {
			for (Chunk[] row : chunks) {
				for (Chunk chunk : row) {
					for (Tile tile : chunk.tiles()) {
						if (tile.actor() != null) {
							tile.actor().particlePool(world.state().particlePool);
						}
					}
				}
			}
		}
	}

	public float nextRandomFloat() {
		return rng.nextFloat();
	}


	public void renderDebug(RenderingEnvironment re) {
		for (PointOfInterest poi : generationProcess.points().points()) {
			poi.render(this, re);
		}
	}

	Chunk getChunk(ChunkCoordinate chunkCoord) {
		assert chunkCoord.zone().equals(coord);
		return chunks[chunkCoord.x()][chunkCoord.y()];
	}

	public void setChunk(int x, int y, Chunk chunk) {
		chunks[x][y] = chunk;
	}

	public Zone[][] getSurroundingZones(World world, int range) {
		Zone[][] zones = new Zone[range * 2 + 1][range * 2 + 1];
		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				zones[x + range][y + range] = world.getZoneObject(coord.add(x, y));
			}
		}
		return zones;
	}

	public BiomeGenerationStep biomeGenerationStep() {
		return generationProcess.biome();
	}

	public PointsGenerationStep pointsGenerationStep() {
		return generationProcess.points();
	}

	public StructureGenerationStep structureGenerationStep() {
		return generationProcess.structure();
	}

	public VillagerGenerationStep villagerGenerationStep() {
		return generationProcess.villager();
	}

	private ConstraintPair indexPosition() {
		return new ConstraintPair(new Vector2f(coord.x() * TILE_HORIZONTAL_SPACING, coord.y() * TILE_VERTICAL_SPACING).scale(ZONE_SIZE * CHUNK_SIZE));
	}

	/**
	 * Returns the absolute position of the top left corner of this zone.
	 *
	 * @return the absolute position of the top left corner of this zone
	 */
	public ConstraintPair pos() {
		return region.pos().add(indexPosition());
	}

	public Tile getTile(TileCoordinate tile) {
		assert tile.zone().equals(coord);
		return chunks[tile.chunk().x()][tile.chunk().y()].getTile(tile);
	}

	public ZoneCoordinate coord() {
		return coord;
	}

	public Region region() {
		return region;
	}

	public Chunk[][] chunks() {
		return chunks;
	}

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	public void reindex(World world) {
		this.world = world;
		this.strategy = world.generation();
		this.region = world.getRegion(coord.region());
		initRNG(world.generation().parameters().seed());
		for (Chunk[] chunkRow : chunks) {
			for (Chunk chunk : chunkRow) {
				if (chunk != null) {
					chunk.reindex(this);
				}
			}
		}
	}

}
