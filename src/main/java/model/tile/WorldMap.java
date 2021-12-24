package model.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import graphics.noise.OpenSimplexNoise;

public class WorldMap {

	private Map<Vector2i, TileChunk> chunks = new HashMap<>();
	private OpenSimplexNoise noise = new OpenSimplexNoise(0);

	/**
	 * @return a copy of the collection of chunks to prevent concurrent modification
	 *         exceptions
	 */
	public List<TileChunk> chunks() {
		return new ArrayList<>(chunks.values());
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

	public TileChunk generateChunk(Vector2i chunkPos) {
		TileType[][] tileTypes = new TileType[16][16];
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				double eval = noise.eval((x + chunkPos.x * 16) * 0.1, (y + (x % 2) * 0.5 + chunkPos.y * 16) * 0.1);
				if (eval < 0.3) {
					tileTypes[y][x] = TileType.WATER;
				} else if (eval < 0.8) {
					tileTypes[y][x] = TileType.GRASS;
				} else {
					tileTypes[y][x] = TileType.SAND;
				}
			}
		}
		return new TileChunk(chunkPos, tileTypes);
	}

}
