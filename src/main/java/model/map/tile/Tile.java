package model.map.tile;

public class Tile {

	private int x;
	private int y;
	private TileType type;

	public Tile(int x, int y, TileType type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public TileType type() {
		return type;
	}

}
