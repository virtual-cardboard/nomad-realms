package model.world;

import static model.world.tile.TileType.*;

import java.util.function.BiFunction;

import model.world.tile.TileType;

public enum Biome {
	OCEAN((Double moisture, Double elevation) -> {
		return SALT_WATER;
	}), ARCTIC((Double moisture, Double elevation) -> {
		return ICE;
	}), TUNDRA((Double moisture, Double elevation) -> {
		return STONE;
	}), RAINFOREST((Double moisture, Double elevation) -> {
		return RICH_GRASS;
	}), SAVANNAH((Double moisture, Double elevation) -> {
		return DRY_GRASS;
	}), FOREST((Double moisture, Double elevation) -> {
		return RICH_GRASS;
	}), TEMPERATE_FOREST((Double moisture, Double elevation) -> {
		return RICH_GRASS;
	}), FRESHWATER((Double moisture, Double elevation) -> {
		return WATER;
	}), TAIGA((Double moisture, Double elevation) -> {
		return SALT_WATER;
	}), GRASSLAND((Double moisture, Double elevation) -> {
		return GRASS;
	}), DESERT((Double moisture, Double elevation) -> {
		return SAND;
	});

	public final BiFunction<Double, Double, TileType> tileTypeFunction;

	private Biome(BiFunction<Double, Double, TileType> tileTypeFunction) {
		this.tileTypeFunction = tileTypeFunction;

	}
}
