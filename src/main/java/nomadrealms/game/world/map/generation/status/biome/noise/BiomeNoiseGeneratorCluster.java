package nomadrealms.game.world.map.generation.status.biome.noise;

import nomadrealms.math.generation.map.LayeredNoise;
import nomadrealms.math.generation.map.NoiseOctave;
import nomadrealms.math.generation.map.OpenSimplexNoise;

/**
 * The cluster of noise generators used to generate the biome map.
 */
public class BiomeNoiseGeneratorCluster {

	private final BiomeNoiseGenerator temperature;
	private final BiomeNoiseGenerator humidity;
	private final BiomeNoiseGenerator continentalness;
	private final BiomeNoiseGenerator erosion;
	private final BiomeNoiseGenerator weirdness;
	private final BiomeNoiseGenerator depth;

	public BiomeNoiseGeneratorCluster(long seed) {
		OpenSimplexNoise base = new OpenSimplexNoise(seed);
		this.temperature = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 0),
				new NoiseOctave(base, 2, 0.02, 1),
				new NoiseOctave(base, 1, 0.3, 2),
				new NoiseOctave(base, 0.5, 0.18, 3),
				new NoiseOctave(base, 0.25, 0.1, 4),
				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 0.05, 0.02, 6)));
		this.humidity = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 0),
				new NoiseOctave(base, 2, 0.02, 1),
				new NoiseOctave(base, 1, 0.3, 2),
				new NoiseOctave(base, 0.5, 0.18, 3),
				new NoiseOctave(base, 0.25, 0.1, 4),
				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 0.05, 0.02, 6)));
		this.continentalness = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 0),
				new NoiseOctave(base, 2, 0.02, 1),
				new NoiseOctave(base, 1, 0.3, 2),
				new NoiseOctave(base, 0.5, 0.18, 3),
				new NoiseOctave(base, 0.25, 0.1, 4),
				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 0.05, 0.02, 6)));
		this.erosion = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 0),
				new NoiseOctave(base, 2, 0.02, 1),
				new NoiseOctave(base, 1, 0.3, 2),
				new NoiseOctave(base, 0.5, 0.18, 3),
				new NoiseOctave(base, 0.25, 0.1, 4),
				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 0.05, 0.02, 6)));
		this.weirdness = new BiomeNoiseGenerator(seed, new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 0),
				new NoiseOctave(base, 2, 0.02, 1),
				new NoiseOctave(base, 1, 0.3, 2),
				new NoiseOctave(base, 0.5, 0.18, 3),
				new NoiseOctave(base, 0.25, 0.1, 4),
				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 0.05, 0.02, 6)));
		this.depth = new BiomeNoiseGenerator(seed, new LayeredNoise(
//				new NoiseOctave(base, 100, 0.4, 0),
//				new NoiseOctave(base, 2, 0.02, 1),
//				new NoiseOctave(base, 1, 0.3, 2),
//				new NoiseOctave(base, 0.5, 0.18, 3),
//				new NoiseOctave(base, 0.25, 0.1, 4),
//				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 5, 1, 6)));
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
