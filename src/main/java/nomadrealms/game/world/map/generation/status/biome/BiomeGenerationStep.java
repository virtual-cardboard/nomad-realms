package nomadrealms.game.world.map.generation.status.biome;

import static nomadrealms.game.world.map.generation.status.GenerationStepStatus.BIOMES;

import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.generation.status.GenerationStep;
import nomadrealms.game.world.map.generation.status.GenerationStepStatus;

public class BiomeGenerationStep extends GenerationStep {

	public BiomeGenerationStep(Zone zone, long worldSeed) {
		super(zone, worldSeed);
	}

	@Override
	public GenerationStepStatus status() {
		return BIOMES;
	}

	@Override
	public void generate(Zone[][] surrounding) {

	}

}
