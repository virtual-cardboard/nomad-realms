package nomadrealms.game.world.map.generation.status;

public enum GenerationStepStatus {

	EMPTY,
	BIOMES,
	POINTS,
	PERTURBANCE(true),
	TERRAIN,
	CARVING(true),
	STRUCTURES,
	SPAWNS,
	AGING(true),
	COMPLETE;

	private boolean nextLayer = false;

	GenerationStepStatus() {
	}

	GenerationStepStatus(boolean nextLayer) {
		this.nextLayer = nextLayer;
	}

	public boolean needsNextLayer() {
		return nextLayer;
	}

}
