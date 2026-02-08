package nomadrealms.context.game.world.map.generation.overworld.structure.config;

import nomadrealms.context.game.actor.types.structure.RockStructure;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationParameters;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;
import nomadrealms.context.game.world.map.generation.overworld.biome.noise.BiomeNoiseGenerator;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType;
import nomadrealms.context.game.world.map.generation.overworld.structure.StructureGenerationConfig;
import nomadrealms.math.generation.map.LayeredNoise;
import nomadrealms.math.generation.map.NoiseOctave;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class RockGenerationConfig extends StructureGenerationConfig {

	private final BiomeNoiseGenerator noise;

	/**
	 * No-arg constructor for serialization.
	 */
	protected RockGenerationConfig() {
		this.noise = null;
	}

	public RockGenerationConfig(MapGenerationParameters mapParameters) {
		OpenSimplexNoise base = new OpenSimplexNoise(mapParameters.seed() + 1);
		this.noise = new BiomeNoiseGenerator(new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 0),
				new NoiseOctave(base, 2, 0.02, 1),
				new NoiseOctave(base, 1, 0.3, 2),
				new NoiseOctave(base, 0.5, 0.18, 3),
				new NoiseOctave(base, 0.25, 0.1, 4),
				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 0.05, 0.02, 6)),
				1);
	}

	@Override
	public Structure placeStructure(TileCoordinate coord, BiomeParameters biome) {
		if (biome.calculateBiomeVariant() == BiomeVariantType.FOREST) {
			if (noise.eval(coord) > 0.3f) {
				return new RockStructure();
			}
		}
		return null;
	}
}
