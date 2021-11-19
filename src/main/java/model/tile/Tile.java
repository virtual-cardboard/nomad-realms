package model.tile;

import common.math.Vector2i;
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

	public Vector2i absPos() {
		return new Vector2i(x, y).add(chunk.pos());
	}

	public TileType type() {
		return type;
	}

	@Override
	public long id() {
		Vector2i cPos = chunk.pos();
		return (((long) x) << 60) | (((long) y) << 56) | (((long) cPos.x) << 28) | cPos.y;
	}

	public TileChunk chunk() {
		return chunk;
	}

	public void setChunk(TileChunk chunk) {
		this.chunk = chunk;
	}

}
