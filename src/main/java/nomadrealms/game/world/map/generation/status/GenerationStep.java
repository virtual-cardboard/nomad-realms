package nomadrealms.game.world.map.generation.status;

import nomadrealms.game.world.map.area.Zone;

public abstract class GenerationStep {

	protected Zone zone;
	protected long worldSeed;

	public GenerationStep(Zone zone, long worldSeed) {
		this.zone = zone;
		this.worldSeed = worldSeed;
	}

	public abstract GenerationStepStatus status();

	public abstract void generate(Zone[][] surrounding);

}
