package nomadrealms.context.game.world.map.generation;

import nomadrealms.context.game.world.map.generation.overworld.biome.noise.BiomeNoiseGeneratorCluster;

public class MapGenerationParameters {

	private long seed;
	private BiomeNoiseGeneratorCluster biomeNoise;

	public MapGenerationParameters() {
	}

	public MapGenerationParameters seed(long seed) {
		this.seed = seed;
		return this;
	}

	public long seed() {
		return seed;
	}

	public MapGenerationParameters biomeNoise(BiomeNoiseGeneratorCluster biomeNoise) {
		this.biomeNoise = biomeNoise;
		return this;
	}

	public BiomeNoiseGeneratorCluster biomeNoise() {
		return biomeNoise;
	}


}
