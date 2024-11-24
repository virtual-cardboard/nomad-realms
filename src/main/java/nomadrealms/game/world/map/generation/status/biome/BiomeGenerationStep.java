package nomadrealms.game.world.map.generation.status.biome;

import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.game.world.map.generation.status.GenerationStepStatus.BIOMES;
import static nomadrealms.game.world.map.generation.status.biome.BiomeType.UNDEFINED;

import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.generation.status.GenerationStep;
import nomadrealms.game.world.map.generation.status.GenerationStepStatus;

/**
 * Generates the biomes for the zone.
 * <br><br>
 * Reference: <a href="https://minecraft.wiki/w/World_generation#Biomes">Minecraft biome generation</a>
 *
 * @author Lunkle
 */
public class BiomeGenerationStep extends GenerationStep {

    public BiomeGenerationStep(Zone zone, long worldSeed) {
        super(zone, worldSeed);
    }

    @Override
    public GenerationStepStatus status() {
        return BIOMES;
    }

    private final BiomeType[][] biomes = new BiomeType[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];

    @Override
    public void generate(Zone[][] surrounding) {
        NoiseGenerator temperatureNoise = new NoiseGenerator(worldSeed);
        NoiseGenerator humidityNoise = new NoiseGenerator(worldSeed);
        NoiseGenerator continentalnessNoise = new NoiseGenerator(worldSeed);
        NoiseGenerator erosionNoise = new NoiseGenerator(worldSeed);
        NoiseGenerator weirdnessNoise = new NoiseGenerator(worldSeed);
        NoiseGenerator depthNoise = new NoiseGenerator(worldSeed);

        for (ChunkCoordinate[] chunkRow : zone.coord().chunkCoordinates()) {
            for (ChunkCoordinate chunk : chunkRow) {
                for (TileCoordinate[] tileRow : chunk.tileCoordinates()) {
                    for (TileCoordinate tile : tileRow) {
                        float temperature = temperatureNoise.eval(tile);
                        float humidity = humidityNoise.eval(tile);
                        float continentalness = continentalnessNoise.eval(tile);
                        float erosion = erosionNoise.eval(tile);
                        float weirdness = weirdnessNoise.eval(tile);
                        float depth = depthNoise.eval(tile);

                        System.out.println("Temperature: " + temperature);
                        System.out.println("Humidity: " + humidity);
                        System.out.println("Continentalness: " + continentalness);
                        System.out.println("Erosion: " + erosion);
                        System.out.println("Weirdness: " + weirdness);
                        System.out.println("Depth: " + depth);

                        biomes[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = UNDEFINED;
                    }
                }
            }
        }
    }

    public BiomeType[][] biomes() {
        return biomes;
    }

}
