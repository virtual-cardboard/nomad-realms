package model.world.tile;

public enum TileType {

	SALT_WATER(0x5cade0ff, 0x5bb4edff),
	ICE(0xebfffeff, 0xfaffffff),
	STONE(0x4d4f4fff, 0x606161ff),
	RICH_GRASS(0x357a35ff, 0x2dad2dff),
	GRASS(0x11d63fff, 0x0ff244ff),
	DRY_GRASS(0x6e8026ff, 0xc2c05dff),
	WATER(0x5daec2ff, 0x66bed4ff),
	SAND(0x9e9747ff, 0xc7be5aff);

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
