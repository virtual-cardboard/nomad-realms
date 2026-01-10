package nomadrealms.context.game.world.map.generation.overworld.structure.config;

import nomadrealms.context.game.actor.structure.RockStructure;
import nomadrealms.context.game.actor.structure.Structure;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationParameters;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;
import nomadrealms.context.game.world.map.generation.overworld.biome.noise.BiomeNoiseGenerator;
import nomadrealms.context.game.world.map.generation.overworld.structure.StructureGenerationConfig;
import nomadrealms.math.generation.map.LayeredNoise;
import nomadrealms.math.generation.map.NoiseOctave;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class RockGenerationConfig extends StructureGenerationConfig {

    private final BiomeNoiseGenerator noise;

    /**
     * No-arg constructor for serialization.
     */
    protected RockGenerationConfig() {
        this.noise = null;
    }

    public RockGenerationConfig(MapGenerationParameters mapParameters) {
        OpenSimplexNoise base = new OpenSimplexNoise(mapParameters.seed() + 1); // Use a different seed
        this.noise = new BiomeNoiseGenerator(new LayeredNoise(
                new NoiseOctave(base, 0.1, 0.5, 0),
                new NoiseOctave(base, 0.5, 0.25, 1),
                new NoiseOctave(base, 1.0, 0.125, 2)
        ), 1);
    }

    @Override
    public Structure placeStructure(TileCoordinate coord, BiomeParameters biome) {
        if (noise.eval(coord) > 0.8f) { // Higher threshold for sparsity
            return new RockStructure();
        }
        return null;
    }
}
