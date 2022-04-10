package model.world.chunk;

import engine.common.math.Vector2i;

public abstract class AbstractTileChunk {

	public static final int CHUNK_SIDE_LENGTH = 16;

	private Vector2i pos;

	public AbstractTileChunk(Vector2i pos) {
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

	public abstract AbstractTileChunk upgrade(AbstractTileChunk[][] neighbours, long worldSeed);

	public abstract int layer();

}
