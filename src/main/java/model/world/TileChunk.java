package model.world;

import static model.world.Tile.TILE_HEIGHT;
import static model.world.Tile.TILE_WIDTH;

import common.math.Vector2i;

public abstract class TileChunk {

	public static final int CHUNK_SIDE_LENGTH = 16;
	public static final int CHUNK_WIDTH = TILE_WIDTH * CHUNK_SIDE_LENGTH * 3 / 4;
	public static final int CHUNK_HEIGHT = TILE_HEIGHT * CHUNK_SIDE_LENGTH;

	private Vector2i pos;

	public TileChunk(Vector2i pos) {
		this.pos = pos;
	}

	public static Vector2i chunkPos(long tileID) {
		int cx = (int) (tileID << 8 >> 36);
		int cy = (int) (tileID << 36 >> 36);
		return new Vector2i(cx, cy);
	}

	public Vector2i pos() {
		return pos;
	}

	public abstract TileChunk upgrade(TileChunk[][] neighbours);

}
