package nomadrealms.game.world.map.generation.status.biome.generator;

import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class ErosionGenerator {

    private long seed;
    private OpenSimplexNoise noise;

    public ErosionGenerator(long seed) {
        this.seed = seed;
        this.noise = new OpenSimplexNoise(seed);
    }

    public static float generateErosion(TileCoordinate coord, long seed) {
        ErosionGenerator generator = new ErosionGenerator(seed);
        double noiseValue = generator.noise.eval(coord.x(), coord.y(), 0);
        return generator.mapNoiseToErosion(noiseValue);
    }

    private float mapNoiseToErosion(double noiseValue) {
        if (noiseValue < -0.78) {
            return 0;
        } else if (noiseValue < -0.375) {
            return 1;
        } else if (noiseValue < -0.2225) {
            return 2;
        } else if (noiseValue < 0.05) {
            return 3;
        } else if (noiseValue < 0.45) {
            return 4;
        } else if (noiseValue < 0.55) {
            return 5;
        } else {
            return 6;
        }
    }
}
