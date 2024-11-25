package nomadrealms.game.world.map.generation.status.biome;

import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.BEACH;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.DEEP_OCEAN;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.ICY_SHORE;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.OCEAN;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.PLAINS;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.ROCKY_SHORE;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.SNOW;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.SNOWY_TUNDRA;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.TAIGA;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.VOID;

import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.generation.status.biome.noise.BiomeNoiseGeneratorCluster;

/**
 * Generates the biomes for the zone.
 * <br><br>
 * Reference: <a href="https://minecraft.wiki/w/World_generation#Biomes">Minecraft biome generation</a>
 *
 * @author Lunkle
 */
public class BiomeGenerationStep {

    private final Zone zone;

    private final BiomeType[][] biomes = new BiomeType[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];
    private final float[][] temperatures = new float[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];
    private final float[][] humidities = new float[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];
    private final float[][] continentalnesses = new float[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];
    private final float[][] erosions = new float[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];
    private final float[][] weirdnesses = new float[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];
    private final float[][] depths = new float[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];

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

                        temperatures[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = temperature;
                        humidities[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = humidity;
                        continentalnesses[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = continentalness;
                        erosions[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = erosion;
                        weirdnesses[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = weirdness;
                        depths[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = depth;

                        biomes[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = decideBiome(temperature, humidity, continentalness, erosion, weirdness, depth);

                    }
                }
            }
        }
    }

    private BiomeType decideBiome(float temperature, float humidity, float continentalness, float erosion, float weirdness, float depth) {
        if (continentalness < -0.5) {
            return DEEP_OCEAN;
        }
        if (continentalness < -0.2) {
            return OCEAN;
        }
        if (continentalness < 0) {
            if (temperature < -0.5) {
                return ICY_SHORE;
            } else {
                if (weirdness < -0.5) {
                    return ROCKY_SHORE;
                }
                return BEACH;
            }
        }
        if (temperature < -0.5) {
            return SNOWY_TUNDRA;
        }
        if (temperature < -0.2) {
            if (humidity < -0.5) {
                return SNOWY_TUNDRA;
            } else {
                return SNOW;
            }
        }
        if (temperature < 0.2) {
            if (humidity < -0.5) {
                return TAIGA;
            } else {
                return PLAINS;
            }
        }
        if (temperature < 0.5) {
            if (humidity < -0.5) {
                return TAIGA;
            } else {
                return PLAINS;
            }
        }
        return VOID;
    }

    public BiomeType[][] biomes() {
        return biomes;
    }

    public BiomeType biomeAt(TileCoordinate coord) {
        return biomes
                [coord.chunk().x() * CHUNK_SIZE + coord.x()]
                [coord.chunk().y() * CHUNK_SIZE + coord.y()];
    }

    public float temperatureAt(TileCoordinate coord) {
        return temperatures
                [coord.chunk().x() * CHUNK_SIZE + coord.x()]
                [coord.chunk().y() * CHUNK_SIZE + coord.y()];
    }

    public float humidityAt(TileCoordinate coord) {
        return humidities
                [coord.chunk().x() * CHUNK_SIZE + coord.x()]
                [coord.chunk().y() * CHUNK_SIZE + coord.y()];
    }

    public float continentalnessAt(TileCoordinate coord) {
        return continentalnesses
                [coord.chunk().x() * CHUNK_SIZE + coord.x()]
                [coord.chunk().y() * CHUNK_SIZE + coord.y()];
    }

    public float erosionAt(TileCoordinate coord) {
        return erosions
                [coord.chunk().x() * CHUNK_SIZE + coord.x()]
                [coord.chunk().y() * CHUNK_SIZE + coord.y()];
    }

    public float weirdnessAt(TileCoordinate coord) {
        return weirdnesses
                [coord.chunk().x() * CHUNK_SIZE + coord.x()]
                [coord.chunk().y() * CHUNK_SIZE + coord.y()];
    }

    public float depthAt(TileCoordinate coord) {
        return depths
                [coord.chunk().x() * CHUNK_SIZE + coord.x()]
                [coord.chunk().y() * CHUNK_SIZE + coord.y()];
    }

}
