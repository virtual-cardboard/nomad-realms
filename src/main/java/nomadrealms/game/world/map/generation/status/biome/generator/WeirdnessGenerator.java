package nomadrealms.game.world.map.generation.status.biome.generator;

import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class WeirdnessGenerator {

    private long seed;
    private OpenSimplexNoise noise;

    public WeirdnessGenerator(long seed) {
        this.seed = seed;
        this.noise = new OpenSimplexNoise(seed);
    }

    public static float generateWeirdness(TileCoordinate coord, long seed) {
        WeirdnessGenerator generator = new WeirdnessGenerator(seed);
        double noiseValue = generator.noise.eval(coord.x(), coord.y(), 0);
        return generator.mapNoiseToWeirdness(noiseValue);
    }

    private float mapNoiseToWeirdness(double noiseValue) {
        if (noiseValue < -0.85) {
            return -1.0f;
        } else if (noiseValue < -0.6) {
            return -0.85f;
        } else if (noiseValue < 0.2) {
            return -0.6f;
        } else if (noiseValue < 0.7) {
            return 0.2f;
        } else {
            return 0.7f;
        }
    }
}
