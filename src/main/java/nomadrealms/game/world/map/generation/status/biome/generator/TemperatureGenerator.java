package nomadrealms.game.world.map.generation.status.biome.generator;

import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class TemperatureGenerator {

	private long seed;
	private OpenSimplexNoise noise;

	public TemperatureGenerator(long seed) {
		this.seed = seed;
		this.noise = new OpenSimplexNoise(seed);
	}

	public float generateTemperature(TileCoordinate coord, long seed) {
		double noiseValue = noise.eval(coord.x(), coord.y(), 0);
		return mapNoiseToTemperature(noiseValue);
	}

	private float mapNoiseToTemperature(double noiseValue) {
		if (noiseValue < -0.45) {
			return 0;
		} else if (noiseValue < -0.15) {
			return 1;
		} else if (noiseValue < 0.2) {
			return 2;
		} else if (noiseValue < 0.55) {
			return 3;
		} else {
			return 4;
		}
	}
}
