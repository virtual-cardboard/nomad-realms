package model.map.tile;

import context.visuals.colour.Colour;

public enum TileType {

	BLANK(Colour.colour(255, 243, 209)),
	GRASS(Colour.colour(15, 242, 68)),
	SAND(Colour.colour(228, 141, 62)),
	WATER(Colour.colour(91, 180, 237));

	private final int colour;

	private TileType(int colour) {
		this.colour = colour;
	}

	public int getColour() {
		return colour;
	}

}
