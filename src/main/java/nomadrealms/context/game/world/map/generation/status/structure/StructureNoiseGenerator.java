package nomadrealms.context.game.world.map.generation.status.structure;

import nomadrealms.context.game.world.map.generation.status.biome.noise.BiomeNoiseGenerator;
import nomadrealms.math.generation.map.LayeredNoise;
import nomadrealms.math.generation.map.NoiseOctave;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class StructureNoiseGenerator {

	private final BiomeNoiseGenerator noise;

	public StructureNoiseGenerator(long seed, float scale) {
		noise = new BiomeNoiseGenerator(seed, new LayeredNoise(new NoiseOctave(new OpenSimplexNoise(seed), 0.5f, 0.5f)), scale);
	}

	public BiomeNoiseGenerator noise() {
		return noise;
	}
}
