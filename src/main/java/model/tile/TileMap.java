package model.tile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import common.math.Vector2i;

public class TileMap {

	private Map<Vector2i, TileChunk> chunks = new HashMap<>();

	public Collection<TileChunk> chunks() {
		return chunks.values();
	}

	public TileChunk chunk(Vector2i chunkPos) {
		return chunks.get(chunkPos);
	}

	public TileChunk chunk(long tileID) {
		return chunk(TileChunk.chunkPos(tileID));
	}

	public void addChunk(TileChunk chunk) {
		chunks.put(chunk.pos(), chunk);
	}

}
