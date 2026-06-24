package nomadrealms.context.game.world.map.generation.overworld;

import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;

import nomadrealms.context.game.world.World;

public abstract class GenerationStep {

	protected transient final Zone zone;
	protected long worldSeed;

	public GenerationStep(Zone zone, long worldSeed) {
		this.zone = zone;
		this.worldSeed = worldSeed;
	}

	public abstract void generate(World world, Zone[][] surrounding, MapGenerationStrategy strategy);

}
