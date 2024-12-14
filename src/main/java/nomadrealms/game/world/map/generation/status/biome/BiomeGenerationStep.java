package nomadrealms.game.world.map.generation.status.biome;

import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
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

import common.math.Vector2i;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
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
public class BiomeGenerationStep {

    private final Zone zone;

    private final BiomeVariantType[][] biomes = new BiomeVariantType[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];

    public BiomeGenerationStep(Zone zone) {
        this.zone = zone;
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
            if (p.depth() < -0.5) {
                return DEEP_OCEAN;
            } else if (p.depth() < 0) {
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
                float closest = Float.MAX_VALUE;
                for (Map.Entry<BiomeCategory, Vector2i> entry : TEMPERATURE_HUMIDITY_VALUES.entrySet()) {
                    Vector2i temperatureHumidity = entry.getValue();
                    int adjustedTemperature = (temperatureHumidity.x() + 1) * (TEMPERATURE_CEIL - TEMPERATURE_FLOOR) / 2 - TEMPERATURE_FLOOR;
                    int adjustedHumidity = (temperatureHumidity.y() + 1) * (HUMIDITY_CEIL - HUMIDITY_FLOOR) / 2 - HUMIDITY_FLOOR;
                    float distance = (float) Math.sqrt(Math.pow(adjustedTemperature - p.temperature(), 2) + Math.pow(adjustedHumidity - p.humidity(), 2));
                    if (distance < closest) {
                        closest = distance;
                        category = entry.getKey();
                    }
                }
                return category;
        }
        throw new IllegalStateException("Could not decide biome category for parameters: " + p);
    }

    public ContinentType decideContinent(BiomeParameters p) {
        if (p.continentalness() < -0.5) {
            return MARINE;
        } else if (p.continentalness() < 0) {
            return LOWLAND;
        } else if (p.continentalness() < 0.5) {
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

}
