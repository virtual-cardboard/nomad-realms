package model.world;

import static model.world.tile.TileType.*;

import model.world.tile.TileType;

public enum Biome {
	OCEAN((m, e) -> SALT_WATER, (m, e, n) -> {
		return null;
	}), ARCTIC((m, e) -> {
		return ICE;
	}, (m, e, n) -> {
		return null;
	}), TUNDRA((m, e) -> {
		return STONE;
	}, (m, e, n) -> {
		return null;
	}), RAINFOREST((m, e) -> {
		return RICH_GRASS;
	}, (m, e, n) -> {
		return null;
	}), SAVANNAH((m, e) -> {
		return DRY_GRASS;
	}, (m, e, n) -> {
		return null;
	}), FOREST((m, e) -> {
		return RICH_GRASS;
	}, (m, e, n) -> {
		return null;
	}), TEMPERATE_FOREST((m, e) -> {
		return RICH_GRASS;
	}, (m, e, n) -> {
		return null;
	}), FRESHWATER((m, e) -> {
		return WATER;
	}, (m, e, n) -> {
		return null;
	}), TAIGA((m, e) -> {
		return SALT_WATER;
	}, (m, e, n) -> {
		return null;
	}), GRASSLAND((m, e) -> {
		return GRASS;
	}, (m, e, n) -> {
		return null;
	}), DESERT((m, e) -> {
		return SAND;
	}, (m, e, n) -> {
		return null;
	});

	public final BiDoubleFunction<TileType> tileTypeFunction;
	private GenerateActorFunction genActorFunction;

	private Biome(BiDoubleFunction<TileType> tileTypeFunction, GenerateActorFunction genActorFunction) {
		this.tileTypeFunction = tileTypeFunction;
		this.genActorFunction = genActorFunction;

	}
}
