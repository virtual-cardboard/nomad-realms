package nomadrealms.context.game.world.map.generation.overworld;

import engine.serialization.Derializable;

@Derializable
public enum GenerationLayer {

	NONE,
	BIOME,
	POINTS,
	STRUCTURE,
	VILLAGER;

	public GenerationLayer previous() {
		if (this == NONE) {
			return null;
		}
		return values()[ordinal() - 1];
	}

	public boolean isAtLeast(GenerationLayer other) {
		return ordinal() >= other.ordinal();
	}

}
