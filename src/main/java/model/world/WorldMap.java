package model.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2f;
import common.math.Vector2i;
import graphics.noise.OpenSimplexNoise;
import model.actor.CardPlayer;
import model.actor.NPC;
import model.state.GameState;

public class WorldMap {

	private static final double variation = 0.02;
	private static final double elevVariation = 0.05;
	private Map<Vector2i, TileChunk> chunks = new HashMap<>();
	private OpenSimplexNoise moistureNoise = new OpenSimplexNoise(0);
	private OpenSimplexNoise elevNoise = new OpenSimplexNoise(1);
	private OpenSimplexNoise actorNoise = new OpenSimplexNoise(3);

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
				if (moisture > 0.7 && elevation <= 0.5) {
					biomeType = Biome.OCEAN;
				} else if (moisture <= 0.3 && elevation > 0.5) {
					biomeType = Biome.DESERT;
				} else {
					biomeType = Biome.PLAINS;
				}
				tileTypes[y][x] = biomeType.tileTypeFunction.apply(elevation, moisture);
			}
		}
		return new TileChunk(chunkPos, tileTypes);
	}

	public List<CardPlayer> generateActors(Vector2i chunkPos, GameState state) {
		List<CardPlayer> actors = new ArrayList<>();
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				double eval = actorNoise.eval((x + chunkPos.x * 16) * 0.1, (y + (x % 2) * 0.5 + chunkPos.y * 16) * 0.1);
				if (eval < 0.1) {
					NPC e = new NPC(6);
					e.setChunkPos(chunkPos);
					e.updatePos(new Vector2f(x * Tile.THREE_QUARTERS_WIDTH, y * Tile.TILE_HEIGHT));
					e.setDirection(new Vector2f(0, 1));
					e.setVelocity(new Vector2f(0, 0));
					actors.add(e);
				}
			}
		}

		return actors;
	}

	public WorldMap copy() {
		WorldMap copy = new WorldMap();
		copy.chunks = new HashMap<>(chunks);
		return copy;
	}

}
