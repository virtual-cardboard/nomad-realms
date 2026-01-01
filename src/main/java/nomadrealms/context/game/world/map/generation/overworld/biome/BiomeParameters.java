package nomadrealms.context.game.world.map.generation.overworld.biome;

import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeCategory.AQUATIC;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeCategory.HUMIDITY_CEIL;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeCategory.HUMIDITY_FLOOR;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeCategory.TEMPERATURE_CEIL;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeCategory.TEMPERATURE_FLOOR;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeCategory.TEMPERATURE_HUMIDITY_VALUES;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType.BEACH;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType.DEEP_OCEAN;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType.DESERT;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType.FOREST;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType.NORMAL_OCEAN;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType.PLAINS;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType.SNOWY_TUNDRA;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType.TAIGA;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType.TEMPERATE_RAINFOREST;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.ContinentType.HIGHLAND;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.ContinentType.LOWLAND;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.ContinentType.MARINE;
import static nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.ContinentType.MIDLAND;

import java.util.Map;

import engine.common.math.Vector2i;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeCategory;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.ContinentType;

/**
 * Represents the parameters used to determine a biome.
 * <br><br>
 * The parameters are:
 * <ul>
 *     <li>Temperature</li>
 *     <li>Humidity</li>
 *     <li>Continentalness</li>
 *     <li>Erosion</li>
 *     <li>Weirdness</li>
 *     <li>Depth</li>
 *
 * @author Lunkle
 */
public class BiomeParameters {

	private final float temperature;
	private final float humidity;
	private final float continentalness;
	private final float erosion;
	private final float weirdness;
	private final float depth;

	/**
	 * No-args constructor for serialization.
	 */
	public BiomeParameters() {
		this.temperature = 0;
		this.humidity = 0;
		this.continentalness = 0;
		this.erosion = 0;
		this.weirdness = 0;
		this.depth = 0;
	}

	public BiomeParameters(float temperature, float humidity, float continentalness, float erosion, float weirdness, float depth) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.continentalness = continentalness;
		this.erosion = erosion;
		this.weirdness = weirdness;
		this.depth = depth;
	}

	public float temperature() {
		return temperature;
	}

	public float humidity() {
		return humidity;
	}

	public float continentalness() {
		return continentalness;
	}

	public float erosion() {
		return erosion;
	}

	public float weirdness() {
		return weirdness;
	}

	public float depth() {
		return depth;
	}

	public ContinentType calculateContinent() {
		if (continentalness() < 0) {
			return MARINE;
		} else if (continentalness() < 0.2) {
			return LOWLAND;
		} else if (continentalness() < 0.6) {
			return MIDLAND;
		} else {
			return HIGHLAND;
		}
	}

	public BiomeCategory calculateBiomeCategory() {
		ContinentType continent = calculateContinent();
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
					float adjustedTemperature = (temperature() + 1) * (TEMPERATURE_CEIL - TEMPERATURE_FLOOR) / 2 + TEMPERATURE_FLOOR;
					float adjustedHumidity = (humidity() + 1) * (HUMIDITY_CEIL - HUMIDITY_FLOOR) / 2 + HUMIDITY_FLOOR;
					double distanceSquared =
							Math.pow(temperatureHumidity.x() - adjustedTemperature, 2) + Math.pow(temperatureHumidity.y() - adjustedHumidity, 2);
					if (distanceSquared < closest) {
						closest = distanceSquared;
						category = entry.getKey();
					}
				}
				return category;
		}
		throw new IllegalStateException("Could not decide biome category for parameters: " + this);
	}

	public BiomeVariantType calculateBiomeVariant() {
		ContinentType continent = calculateContinent();
		BiomeCategory category = calculateBiomeCategory();

		if (continent == MARINE) {
			if (depth() < -0.1) {
				return DEEP_OCEAN;
			} else if (depth() < 0.6) {
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
				throw new IllegalStateException("Could not decide biome variant for parameters: " + this + " and " +
						"category: " + category + " and continent: " + continent);
		}
	}


	@Override
	public String toString() {
		return "BiomeParameters{" +
				"temperature=" + temperature +
				", humidity=" + humidity +
				", continentalness=" + continentalness +
				", erosion=" + erosion +
				", weirdness=" + weirdness +
				", depth=" + depth +
				'}';
	}

}
