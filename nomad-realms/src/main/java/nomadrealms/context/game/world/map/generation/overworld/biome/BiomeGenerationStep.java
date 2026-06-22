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

	private BiomeVariantType[][] biomes = new BiomeVariantType[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];

	private transient BiomeNoiseGeneratorCluster noise;


	/**
	 * No-arg constructor for serialization.
	 */
	protected BiomeGenerationStep() {
		super(null, 0);
	}

	public BiomeGenerationStep(Zone zone, MapGenerationStrategy strategy) {
		super(zone, strategy.parameters().seed());
		this.noise = strategy.parameters().biomeNoise();
	}

	@Override
	public void generate(MapGenerationStrategy strategy) {
		if (this.noise == null) {
			this.noise = strategy.parameters().biomeNoise();
		}
		for (int chunkX = 0; chunkX < ZONE_SIZE; chunkX++) {
			for (int chunkY = 0; chunkY < ZONE_SIZE; chunkY++) {
				ChunkCoordinate chunkCoord = new ChunkCoordinate(zone.coord(), chunkX, chunkY);
				for (int tileX = 0; tileX < CHUNK_SIZE; tileX++) {
					for (int tileY = 0; tileY < CHUNK_SIZE; tileY++) {
						TileCoordinate tileCoord = new TileCoordinate(chunkCoord, tileX, tileY);
						int x = chunkX * CHUNK_SIZE + tileX;
						int y = chunkY * CHUNK_SIZE + tileY;
						biomes[x][y] = parametersAt(tileCoord).calculateBiomeVariant();
					}
				}
			}
		}
	}

	public BiomeVariantType[][] biomes() {
		return biomes;
	}

	public void clear() {
		biomes = null;
	}

	public BiomeVariantType biomeAt(TileCoordinate coord) {
		return biomes
				[coord.chunk().x() * CHUNK_SIZE + coord.x()]
				[coord.chunk().y() * CHUNK_SIZE + coord.y()];
	}

	@Override
	public void reindex(Zone zone) {
		this.noise = zone.region().world().generation().parameters().biomeNoise();
	}

	public BiomeParameters parametersAt(TileCoordinate coord) {
		if (noise == null) {
			// This is only called when noise is null after deserialization before reindex.
			// It's a fallback.
			noise = new BiomeNoiseGeneratorCluster(worldSeed, 0.002f);
		}
		float temperature = noise.temperature().eval(coord);
		float humidity = noise.humidity().eval(coord);
		float continentalness = noise.continentalness().eval(coord);
		float erosion = noise.erosion().eval(coord);
		float weirdness = noise.weirdness().eval(coord);
		float depth = noise.depth().eval(coord);

		return new BiomeParameters(temperature, humidity, continentalness, erosion, weirdness, depth);
	}

	public BiomeCategory categoryAt(TileCoordinate coord) {
		return parametersAt(coord).calculateBiomeCategory();
	}

	public ContinentType continentAt(TileCoordinate coord) {
		return parametersAt(coord).calculateContinent();
	}

}
