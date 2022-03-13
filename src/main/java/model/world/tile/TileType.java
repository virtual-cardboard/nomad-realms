package model.world.tile;

import static context.visuals.colour.Colour.rgb;

public enum TileType {

	BLANK(rgb(237, 232, 218), rgb(255, 243, 209)),
	GRASS(rgb(17, 214, 63), rgb(15, 242, 68)),
	SAND(rgb(204, 127, 57), rgb(228, 141, 62)),
	WATER(rgb(92, 173, 224), rgb(91, 180, 237));

	private final int outlineColour;
	private final int colour;

	private TileType(int outlineColour, int colour) {
		this.outlineColour = outlineColour;
		this.colour = colour;
	}

	public int outlineColour() {
		return outlineColour;
	}

	public int colour() {
		return colour;
	}

}
