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
import nomadrealms.game.world.map.generation.status.biome.generator.ContinentalnessGenerator;
import nomadrealms.game.world.map.generation.status.biome.generator.DepthGenerator;
import nomadrealms.game.world.map.generation.status.biome.generator.ErosionGenerator;
import nomadrealms.game.world.map.generation.status.biome.generator.HumidityGenerator;
import nomadrealms.game.world.map.generation.status.biome.generator.WeirdnessGenerator;

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
        NoiseGenerator temperature = new NoiseGenerator(worldSeed);
        HumidityGenerator humidity = new HumidityGenerator(worldSeed);
        ContinentalnessGenerator continentalness = new ContinentalnessGenerator(worldSeed);
        ErosionGenerator erosion = new ErosionGenerator(worldSeed);
        WeirdnessGenerator weirdness = new WeirdnessGenerator(worldSeed);
        DepthGenerator depth = new DepthGenerator(worldSeed);

        for (ChunkCoordinate[] chunkRow : zone.coord().chunkCoordinates()) {
            for (ChunkCoordinate chunk : chunkRow) {
                for (TileCoordinate[] tileRow : chunk.tileCoordinates()) {
                    for (TileCoordinate tile : tileRow) {
//                        float temperature = temperatureGenerator.generateTemperature(tile, worldSeed);
//                        float humidity = humidityGenerator.generateHumidity(tile, worldSeed);
//                        float continentalness = continentalnessGenerator.generateContinentalness(tile, worldSeed);
//                        float erosion = erosionGenerator.generateErosion(tile, worldSeed);
//                        float weirdness = weirdnessGenerator.generateWeirdness(tile, worldSeed);
//                        float depth = depthGenerator.generateDepth(tile, worldSeed);

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
