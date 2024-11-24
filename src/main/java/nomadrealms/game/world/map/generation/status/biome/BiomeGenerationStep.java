package nomadrealms.game.world.map.generation.status.biome;

import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.UNDEFINED;

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

                        biomes[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = decideBiome(temperature, humidity, continentalness, erosion, weirdness, depth);
                    }
                }
            }
        }
    }

    private BiomeType decideBiome(float temperature, float humidity, float continentalness, float erosion, float weirdness, float depth) {
        if (depth < -0.5) {
            return BiomeType.DEEP_OCEAN;
        } else if (depth < 0) {
            return BiomeType.OCEAN;
        } else if (continentalness < -0.5) {
            return BiomeType.BEACH;
        } else if (temperature < 0.2) {
            if (humidity < 0.3) {
                return BiomeType.SNOWY_TUNDRA;
            } else {
                return BiomeType.SNOWY_MOUNTAINS;
            }
        } else if (temperature < 0.5) {
            if (humidity < 0.3) {
                return BiomeType.TAIGA;
            } else {
                return BiomeType.FOREST;
            }
        } else if (temperature < 0.8) {
            if (humidity < 0.3) {
                return BiomeType.SAVANNA;
            } else {
                return BiomeType.JUNGLE;
            }
        } else {
            if (humidity < 0.3) {
                return BiomeType.DESERT;
            } else {
                return BiomeType.SWAMP;
            }
        }
    }

    public BiomeType[][] biomes() {
        return biomes;
    }

    public BiomeType biomeAt(TileCoordinate coord) {
        return biomes
                [coord.chunk().x() * CHUNK_SIZE + coord.x()]
                [coord.chunk().y() * CHUNK_SIZE + coord.y()];
    }

}
