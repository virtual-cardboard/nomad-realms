package model.world;

import java.util.function.BiFunction;

import model.world.tile.TileType;

public enum Biome {
	PLAINS((Double moisture, Double elevation) -> {
		return TileType.GRASS;
	}), OCEAN((Double moisture, Double elevation) -> {
		return TileType.WATER;
	}), DESERT((Double moisture, Double elevation) -> {
		return TileType.SAND;
	});

	public final BiFunction<Double, Double, TileType> tileTypeFunction;

	private Biome(BiFunction<Double, Double, TileType> tileTypeFunction) {
		this.tileTypeFunction = tileTypeFunction;

	}
}
