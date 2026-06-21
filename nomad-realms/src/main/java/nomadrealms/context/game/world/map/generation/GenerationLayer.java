package nomadrealms.context.game.world.map.generation;

public enum GenerationLayer {

	NONE,
	BIOME,
	POINTS,
	STRUCTURE,
	VILLAGER,
	FINISHED;

	public GenerationLayer next() {
		if (this == FINISHED) {
			return FINISHED;
		}
		return values()[ordinal() + 1];
	}

	public GenerationLayer previous() {
		if (this == NONE) {
			return NONE;
		}
		return values()[ordinal() - 1];
	}

}
