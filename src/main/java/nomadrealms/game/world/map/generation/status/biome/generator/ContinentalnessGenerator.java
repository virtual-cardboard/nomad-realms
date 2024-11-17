package nomadrealms.game.world.map.generation.status.biome.generator;

import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class ContinentalnessGenerator {

    private long seed;
    private OpenSimplexNoise noise;

    public ContinentalnessGenerator(long seed) {
        this.seed = seed;
        this.noise = new OpenSimplexNoise(seed);
    }

    public static float generateContinentalness(TileCoordinate coord, long seed) {
        ContinentalnessGenerator generator = new ContinentalnessGenerator(seed);
        double noiseValue = generator.noise.eval(coord.x(), coord.y(), 0);
        return generator.mapNoiseToContinentalness(noiseValue);
    }

    private float mapNoiseToContinentalness(double noiseValue) {
        if (noiseValue < -1.05) {
            return -1.2f;
        } else if (noiseValue < -0.455) {
            return -1.05f;
        } else if (noiseValue < -0.19) {
            return -0.455f;
        } else if (noiseValue < -0.11) {
            return -0.19f;
        } else if (noiseValue < 0.03) {
            return -0.11f;
        } else if (noiseValue < 0.3) {
            return 0.03f;
        } else {
            return 0.3f;
        }
    }
}
