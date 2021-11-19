package model.tile;

import common.math.Vector2i;
import common.math.Vector2l;
import model.GameObject;

public class Tile extends GameObject {

	public static final float TILE_WIDTH = 80;
	public static final float TILE_HEIGHT = 63;
	public static final float TILE_OUTLINE = 3;

	private int x;
	private int y;
	private TileType type;
	private TileChunk chunk;

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

	public Vector2l absPos() {
		return new Vector2l(x, y).add(chunk.pos());
	}

	public TileType type() {
		return type;
	}

	@Override
	public long id() {
		Vector2l cPos = chunk.pos();
		return (((long) x) << 60) | (((long) y) << 56) | ((cPos.x) << 28) | cPos.y;
	}

	public static Vector2i tilePos(long tileID) {
		int x = (int) ((tileID >>> 60) & 0b1111);
		int y = (int) ((tileID >>> 56) & 0b1111);
		return new Vector2i(x, y);
	}

	public TileChunk chunk() {
		return chunk;
	}

	public void setChunk(TileChunk chunk) {
		this.chunk = chunk;
	}

}
