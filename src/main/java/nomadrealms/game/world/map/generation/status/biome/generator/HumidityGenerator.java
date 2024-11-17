package nomadrealms.game.world.map.generation.status.biome.generator;

import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class HumidityGenerator {

    private long seed;
    private OpenSimplexNoise noise;

    public HumidityGenerator(long seed) {
        this.seed = seed;
        this.noise = new OpenSimplexNoise(seed);
    }

    public float generateHumidity(TileCoordinate coord, long seed) {
        double noiseValue = noise.eval(coord.x(), coord.y(), 0);
        return mapNoiseToHumidity(noiseValue);
    }

    private float mapNoiseToHumidity(double noiseValue) {
        if (noiseValue < -0.35) {
            return 0;
        } else if (noiseValue < -0.1) {
            return 1;
        } else if (noiseValue < 0.1) {
            return 2;
        } else if (noiseValue < 0.3) {
            return 3;
        } else {
            return 4;
        }
    }
}
