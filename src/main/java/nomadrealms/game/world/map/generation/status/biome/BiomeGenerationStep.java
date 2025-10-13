package nomadrealms.game.world.map.generation.status.biome;

import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.game.world.map.generation.status.GenerationStepStatus.BIOMES;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.AQUATIC;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.HUMIDITY_CEIL;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.HUMIDITY_FLOOR;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.TEMPERATURE_CEIL;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.TEMPERATURE_FLOOR;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.TEMPERATURE_HUMIDITY_VALUES;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType.BEACH;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType.DEEP_OCEAN;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType.DESERT;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType.FOREST;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType.NORMAL_OCEAN;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType.PLAINS;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType.SNOWY_TUNDRA;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType.TAIGA;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType.TEMPERATE_RAINFOREST;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.ContinentType.HIGHLAND;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.ContinentType.LOWLAND;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.ContinentType.MARINE;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.ContinentType.MIDLAND;

import java.util.Map;

import engine.common.math.Vector2i;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.generation.status.GenerationStep;
import nomadrealms.game.world.map.generation.status.GenerationStepStatus;
import nomadrealms.game.world.map.generation.status.biome.noise.BiomeNoiseGeneratorCluster;
import nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory;
import nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType;
import nomadrealms.game.world.map.generation.status.biome.nomenclature.ContinentType;

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
		super(null, 0);
	}

	public BiomeGenerationStep(Zone zone, long seed) {
		super(zone, seed);
	}

	@Override
	public GenerationStepStatus status() {
		return BIOMES;
	}

	@Override
	public void generate(Zone[][] surrounding) {
	}

	public void generate(BiomeNoiseGeneratorCluster noise, Zone[][] surrounding) {
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

						this.parameters[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] =
								parameters;
						this.continents[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = decideContinent(parameters);
						this.categories[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = decideBiomeCategory(parameters);
						biomes[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = decideBiomeVariant(parameters);
					}
				}
			}
		}
	}

	private BiomeVariantType decideBiomeVariant(BiomeParameters p) {
		ContinentType continent = decideContinent(p);
		BiomeCategory category = decideBiomeCategory(p);

		if (continent == MARINE) {
			if (p.depth() < -0.1) {
				return DEEP_OCEAN;
			} else if (p.depth() < 0.6) {
				return NORMAL_OCEAN;
			} else {
				return BEACH;
			}
		}
		switch (category) {
			case AQUATIC:
				// Unreachable code for now, should catch in previous if statement
				return NORMAL_OCEAN;
			case RAINFOREST:
				return TEMPERATE_RAINFOREST;
			case GRASSLAND:
				return PLAINS;
			case CONIFEROUS_FOREST:
				return TAIGA;
			case TEMPERATE_DECIDUOUS_FOREST:
				return FOREST;
			case DESERT:
				return DESERT;
			case TUNDRA:
				return SNOWY_TUNDRA;
			default:
				throw new IllegalStateException("Could not decide biome variant for parameters: " + p + " and " +
						"category: " + category + " and continent: " + continent);
		}
	}

	public BiomeCategory decideBiomeCategory(BiomeParameters p) {
		ContinentType continent = decideContinent(p);
		switch (continent) {
			case MARINE:
				return AQUATIC;
			case LOWLAND:
			case MIDLAND:
			case HIGHLAND:
				BiomeCategory category = null;
				double closest = Double.MAX_VALUE;
				for (Map.Entry<BiomeCategory, Vector2i> entry : TEMPERATURE_HUMIDITY_VALUES.entrySet()) {
					Vector2i temperatureHumidity = entry.getValue();
					float adjustedTemperature = (p.temperature() + 1) * (TEMPERATURE_CEIL - TEMPERATURE_FLOOR) / 2 + TEMPERATURE_FLOOR;
					float adjustedHumidity = (p.humidity() + 1) * (HUMIDITY_CEIL - HUMIDITY_FLOOR) / 2 + HUMIDITY_FLOOR;
					double distanceSquared =
							Math.pow(temperatureHumidity.x() - adjustedTemperature, 2) + Math.pow(temperatureHumidity.y() - adjustedHumidity, 2);
					if (distanceSquared < closest) {
						closest = distanceSquared;
						category = entry.getKey();
					}
				}
				return category;
		}
		throw new IllegalStateException("Could not decide biome category for parameters: " + p);
	}

	public ContinentType decideContinent(BiomeParameters p) {
		if (p.continentalness() < 0) {
			return MARINE;
		} else if (p.continentalness() < 0.2) {
			return LOWLAND;
		} else if (p.continentalness() < 0.6) {
			return MIDLAND;
		} else {
			return HIGHLAND;
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
