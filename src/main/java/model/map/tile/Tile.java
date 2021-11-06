package model.map.tile;

import model.GameObject;

public class Tile extends GameObject {

	public static final float TILE_WIDTH = 80;
	public static final float TILE_HEIGHT = 63;
	public static final float TILE_OUTLINE = 3;

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

	@Override
	public long id() {
		// TODO:
		// 4 bits, tileX
		// 4 bits, tileY
		// 28 bits, chunkX
		// 28 bits, chunkY
		int chunkX = 0;
		int chunkY = 0;
		return (((long) x) << 60) | (((long) y) << 56) | (((long) chunkX) << 28) | chunkY;
	}

}
