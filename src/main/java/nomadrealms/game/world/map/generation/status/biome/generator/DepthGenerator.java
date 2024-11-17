package nomadrealms.game.world.map.generation.status.biome.generator;

import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class DepthGenerator {

    private long seed;
    private OpenSimplexNoise noise;

    public DepthGenerator(long seed) {
        this.seed = seed;
        this.noise = new OpenSimplexNoise(seed);
    }

    public float generateDepth(TileCoordinate coord, long seed) {
        double noiseValue = noise.eval(coord.x(), coord.y(), 0);
        return mapNoiseToDepth(noiseValue);
    }

    private float mapNoiseToDepth(double noiseValue) {
        if (noiseValue < 0.2) {
            return 0.0f;
        } else if (noiseValue < 0.9) {
            return 0.2f;
        } else if (noiseValue < 1.0) {
            return 0.9f;
        } else {
            return 1.1f;
        }
    }
}
