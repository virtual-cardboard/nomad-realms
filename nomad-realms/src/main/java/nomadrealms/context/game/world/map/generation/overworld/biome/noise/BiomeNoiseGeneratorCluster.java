package nomadrealms.context.game.world.map.generation.overworld.biome.noise;

import nomadrealms.math.generation.map.LayeredNoise;
import nomadrealms.math.generation.map.NoiseOctave;
import nomadrealms.math.generation.map.OpenSimplexNoise;

/**
 * The cluster of {@link BiomeNoiseGenerator}s used to generate the biome map.
 */
public class BiomeNoiseGeneratorCluster {

	private final BiomeNoiseGenerator temperature;
	private final BiomeNoiseGenerator humidity;
	private final BiomeNoiseGenerator continentalness;
	private final BiomeNoiseGenerator erosion;
	private final BiomeNoiseGenerator weirdness;
	private final BiomeNoiseGenerator depth;
	private final BiomeNoiseGenerator river;

	/**
	 * No-arg constructor for serialization.
	 */
	protected BiomeNoiseGeneratorCluster() {
		temperature = null;
		humidity = null;
		continentalness = null;
		erosion = null;
		weirdness = null;
		depth = null;
		river = null;
	}

	public BiomeNoiseGeneratorCluster(long seed) {
		this(seed, 1);
	}

	public BiomeNoiseGeneratorCluster(long seed, float frequency) {
		OpenSimplexNoise base = new OpenSimplexNoise(seed);
		this.temperature = new BiomeNoiseGenerator(new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 0),
				new NoiseOctave(base, 2, 0.02, 1),
				new NoiseOctave(base, 1, 0.3, 2),
				new NoiseOctave(base, 0.5, 0.18, 3),
				new NoiseOctave(base, 0.25, 0.1, 4),
				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 0.05, 0.02, 6)),
				frequency);
		this.humidity = new BiomeNoiseGenerator(new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 7),
				new NoiseOctave(base, 2, 0.02, 8),
				new NoiseOctave(base, 1, 0.3, 9),
				new NoiseOctave(base, 0.5, 0.18, 10),
				new NoiseOctave(base, 0.25, 0.1, 11),
				new NoiseOctave(base, 0.1, 0.05, 12),
				new NoiseOctave(base, 0.05, 0.02, 13)),
				frequency);
		this.continentalness = new BiomeNoiseGenerator(new LayeredNoise(
				new NoiseOctave(base, 1, 0.025, 14),
				new NoiseOctave(base, 0.25, 0.975, 15)),
				frequency, 3);
		this.erosion = new BiomeNoiseGenerator(new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 16),
				new NoiseOctave(base, 2, 0.02, 17),
				new NoiseOctave(base, 1, 0.3, 18),
				new NoiseOctave(base, 0.5, 0.18, 19),
				new NoiseOctave(base, 0.25, 0.1, 20),
				new NoiseOctave(base, 0.1, 0.05, 21),
				new NoiseOctave(base, 0.05, 0.02, 22)),
				frequency);
		this.weirdness = new BiomeNoiseGenerator(new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 23),
				new NoiseOctave(base, 2, 0.02, 24),
				new NoiseOctave(base, 1, 0.3, 25),
				new NoiseOctave(base, 0.5, 0.18, 26),
				new NoiseOctave(base, 0.25, 0.1, 27),
				new NoiseOctave(base, 0.1, 0.05, 28),
				new NoiseOctave(base, 0.05, 0.02, 29)),
				frequency);
		this.depth = new BiomeNoiseGenerator(new LayeredNoise(
//				new NoiseOctave(base, 100, 0.4, 0),
//				new NoiseOctave(base, 2, 0.02, 1),
//				new NoiseOctave(base, 1, 0.3, 2),
//				new NoiseOctave(base, 0.5, 0.18, 3),
//				new NoiseOctave(base, 0.25, 0.1, 4),
//				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 5, 1, 30)),
				frequency);
		this.river = new BiomeNoiseGenerator(new LayeredNoise(
				new NoiseOctave(base, 10, 1, 31)),
				frequency);
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

	public BiomeNoiseGenerator river() {
		return river;
	}

}
