package nomadrealms.context.game.world.map.generation.status;

import nomadrealms.context.game.world.map.area.Zone;

public abstract class GenerationStep {

	protected transient final Zone zone;
	protected long worldSeed;

	public GenerationStep(Zone zone, long worldSeed) {
		this.zone = zone;
		this.worldSeed = worldSeed;
	}

	public abstract GenerationStepStatus status();

	public abstract void generate(Zone[][] surrounding);

}
