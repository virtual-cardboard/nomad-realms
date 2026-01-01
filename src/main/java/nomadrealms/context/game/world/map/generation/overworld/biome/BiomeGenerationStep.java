package nomadrealms.context.game.world.map.generation.overworld.biome;

import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.biome.noise.BiomeNoiseGeneratorCluster;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeCategory;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.ContinentType;

/**
 * Generates the biomes for the zone.
 * <br><br>
 * Reference: <a href="https://minecraft.wiki/w/World_generation#Biomes">Minecraft biome generation</a>
 *
 * @author Lunkle
 */
public class BiomeGenerationStep extends GenerationStep {

	private transient final BiomeParameters[][] parameters =
			new BiomeParameters[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];
	private transient final ContinentType[][] continents = new ContinentType[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];
	private transient final BiomeCategory[][] categories = new BiomeCategory[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];
	private final BiomeVariantType[][] biomes = new BiomeVariantType[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];


	/**
	 * No-arg constructor for serialization.
	 */
	protected BiomeGenerationStep() {
		this(null, 0);
	}

	public BiomeGenerationStep(Zone zone, long seed) {
		super(zone, seed);
	}

	@Override
	public void generate(Zone[][] surrounding, MapGenerationStrategy strategy) {
		BiomeNoiseGeneratorCluster noise = strategy.parameters().biomeNoise();

		for (ChunkCoordinate[] chunkRow : zone.coord().chunkCoordinates()) {
			for (ChunkCoordinate chunk : chunkRow) {
				for (TileCoordinate[] tileRow : chunk.tileCoordinates()) {
					for (TileCoordinate tile : tileRow) {
						float temperature = noise.temperature().eval(tile);
						float humidity = noise.humidity().eval(tile);
						float continentalness = noise.continentalness().eval(tile);
						float erosion = noise.erosion().eval(tile);
						float weirdness = noise.weirdness().eval(tile);
						float depth = noise.depth().eval(tile);

						BiomeParameters parameters = new BiomeParameters(temperature, humidity, continentalness, erosion, weirdness, depth);

						int x = chunk.x() * CHUNK_SIZE + tile.x();
						int y = chunk.y() * CHUNK_SIZE + tile.y();
						this.parameters[x][y] = parameters;
						this.continents[x][y] = parameters.calculateContinent();
						this.categories[x][y] = parameters.calculateBiomeCategory();
						biomes[x][y] = parameters.calculateBiomeVariant();
					}
				}
			}
		}
	}

	public BiomeVariantType[][] biomes() {
		return biomes;
	}

	public BiomeVariantType biomeAt(TileCoordinate coord) {
		return biomes
				[coord.chunk().x() * CHUNK_SIZE + coord.x()]
				[coord.chunk().y() * CHUNK_SIZE + coord.y()];
	}

	public BiomeParameters parametersAt(TileCoordinate coord) {
		return parameters
				[coord.chunk().x() * CHUNK_SIZE + coord.x()]
				[coord.chunk().y() * CHUNK_SIZE + coord.y()];
	}

	public BiomeCategory categoryAt(TileCoordinate coord) {
		return categories
				[coord.chunk().x() * CHUNK_SIZE + coord.x()]
				[coord.chunk().y() * CHUNK_SIZE + coord.y()];
	}

	public ContinentType continentAt(TileCoordinate coord) {
		return continents
				[coord.chunk().x() * CHUNK_SIZE + coord.x()]
				[coord.chunk().y() * CHUNK_SIZE + coord.y()];
	}

}
