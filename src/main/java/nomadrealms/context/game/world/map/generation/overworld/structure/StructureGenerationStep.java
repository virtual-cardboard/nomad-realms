package nomadrealms.context.game.world.map.generation.overworld.structure;

import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;

public class StructureGenerationStep extends GenerationStep {

	/**
	 * No-arg constructor for serialization.
	 */
	protected StructureGenerationStep() {
		super(null, 0);
	}

	public StructureGenerationStep(Zone zone, long seed) {
		super(zone, seed);
	}

	@Override
	public void generate(Zone[][] surrounding, MapGenerationStrategy strategy) {

	}
}
