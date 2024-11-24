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

//                        System.out.println("Temperature: " + temperature);
//                        System.out.println("Humidity: " + humidity);
//                        System.out.println("Continentalness: " + continentalness);
//                        System.out.println("Erosion: " + erosion);
//                        System.out.println("Weirdness: " + weirdness);
//                        System.out.println("Depth: " + depth);

                        biomes[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = UNDEFINED;
                    }
                }
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
