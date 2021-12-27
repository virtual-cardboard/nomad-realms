package model.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import graphics.noise.OpenSimplexNoise;

public class WorldMap {

	private static final double variation = 0.05;
	private static final double elevVariation = 0.05;
	private Map<Vector2i, TileChunk> chunks = new HashMap<>();
	private OpenSimplexNoise moistureNoise = new OpenSimplexNoise(0);
	private OpenSimplexNoise elevNoise = new OpenSimplexNoise(1);
	private OpenSimplexNoise biomeNoise = new OpenSimplexNoise(2);

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
		Biome biomeType;
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				double moisture = moistureNoise.eval((x + chunkPos.x * 16) * variation,
						(y + (x % 2) * 0.5 + chunkPos.y * 16) * variation);
				double elevation = elevNoise.eval((x + chunkPos.x * 16) * elevVariation,
						(y + (x % 2) * 0.5 + chunkPos.y * 16) * elevVariation);
				double biomeEval = biomeNoise.eval((x + chunkPos.x * 16) * elevVariation,
						(y + (x % 2) * 0.5 + chunkPos.y * 16) * elevVariation);
				if (biomeEval < 0.3) {
					biomeType = Biome.PLAINS;
				} else if (biomeEval < 0.8) {
					biomeType = Biome.OCEAN;
				} else {
					biomeType = Biome.DESERT;
				}
				tileTypes[y][x] = biomeType.tileTypeFunction.apply(elevation, moisture);
			}
		}
		return new TileChunk(chunkPos, tileTypes);
	}

}
