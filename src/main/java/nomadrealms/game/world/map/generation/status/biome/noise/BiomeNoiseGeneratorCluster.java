package nomadrealms.game.world.map.generation.status.biome.noise;

import nomadrealms.math.generation.map.LayeredNoise;
import nomadrealms.math.generation.map.NoiseOctave;
import nomadrealms.math.generation.map.OpenSimplexNoise;

/**
 * The cluster of noise generators used to generate the biome map.
 */
public class BiomeNoiseGeneratorCluster {

	private static final double FREQUENCY_SCALE = 50;

	private final BiomeNoiseGenerator temperature;
	private final BiomeNoiseGenerator humidity;
	private final BiomeNoiseGenerator continentalness;
	private final BiomeNoiseGenerator erosion;
	private final BiomeNoiseGenerator weirdness;
	private final BiomeNoiseGenerator depth;

	public BiomeNoiseGeneratorCluster(long seed) {
		OpenSimplexNoise base = new OpenSimplexNoise(seed);
		this.temperature = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, FREQUENCY_SCALE * 0.05, 0.1, 0),
				new NoiseOctave(base, FREQUENCY_SCALE * 0.005, 0.9, 1)));
		this.humidity = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, FREQUENCY_SCALE * 0.05, 0.1, 2),
				new NoiseOctave(base, FREQUENCY_SCALE * 0.005, 0.9, 3)));
		this.continentalness = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, FREQUENCY_SCALE * 0.001, 1, 5)));
		this.erosion = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, FREQUENCY_SCALE * 0.05, 0.1, 6),
				new NoiseOctave(base, FREQUENCY_SCALE * 0.05, 0.9, 7)));
		this.weirdness = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, FREQUENCY_SCALE * 0.05, 0.1, 8),
				new NoiseOctave(base, FREQUENCY_SCALE * 0.05, 0.9, 9)));
		this.depth = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, FREQUENCY_SCALE * 0.25, 0.1, 10),
				new NoiseOctave(base, FREQUENCY_SCALE * 0.05, 0.9, 11)));
	}

	public BiomeNoiseGenerator temperature() {
		return temperature;
	}

	public BiomeNoiseGenerator humidity() {
		return humidity;
	}

	public BiomeNoiseGenerator continentalness() {
		return continentalness;
	}

	public BiomeNoiseGenerator erosion() {
		return erosion;
	}

	public BiomeNoiseGenerator weirdness() {
		return weirdness;
	}

	public BiomeNoiseGenerator depth() {
		return depth;
	}

}
