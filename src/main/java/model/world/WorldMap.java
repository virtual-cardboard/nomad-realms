package model.world;

import static context.game.visuals.GameCamera.RENDER_RADIUS;
import static model.world.chunk.AbstractTileChunk.chunkPos;
import static model.world.chunk.TileChunk.FINAL_LAYER_NUMBER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import math.WorldPos;
import model.state.GameState;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.TileChunk;
import model.world.chunk.generatebiomes.GenerateBiomesChunk;
import model.world.tile.Tile;

public class WorldMap {

	private Map<Vector2i, AbstractTileChunk> chunks = new HashMap<>();
	private long worldSeed = 0;
	private int maxLayer = FINAL_LAYER_NUMBER;

	/**
	 * @return a copy of the collection of chunks to prevent concurrent modification
	 *         exceptions
	 */
	public List<AbstractTileChunk> chunks() {
		return new ArrayList<>(chunks.values());
	}

	public TileChunk chunk(Vector2i chunkPos) {
		AbstractTileChunk chunk = chunks.get(chunkPos);
		if (chunk == null) {
			return null;
		}
		return chunk.layer() == FINAL_LAYER_NUMBER ? (TileChunk) chunk : null;
	}

	public TileChunk chunk(long tileID) {
		return chunk(chunkPos(tileID));
	}

	public void addChunk(AbstractTileChunk chunk) {
		chunks.put(chunk.pos(), chunk);
	}

	public Tile tile(WorldPos worldPos) {
		return chunk(worldPos.chunkPos()).tile(worldPos.tilePos());
	}

	public void generateTerrainAround(Vector2i around, GameState nextState) {
		for (int layer = 0; layer <= maxLayer; layer++) {
			for (int y = -RENDER_RADIUS - maxLayer + layer; y <= RENDER_RADIUS + maxLayer - layer; y++) {
				for (int x = -RENDER_RADIUS - maxLayer + layer; x <= RENDER_RADIUS + maxLayer - layer; x++) {
					Vector2i chunkPos = around.add(x, y);
					if (chunks.get(chunkPos) == null) {
						chunks.put(chunkPos, GenerateBiomesChunk.create(chunkPos, worldSeed));
					}
					while (chunks.get(chunkPos).layer() < layer) {
						AbstractTileChunk[][] neighbours = { { null, null, null }, { null, null, null }, { null, null, null } };
						for (int row = -1; row <= 1; row++) {
							for (int col = -1; col <= 1; col++) {
								neighbours[row + 1][col + 1] = chunks.get(chunkPos.add(col, row));
							}
						}
						AbstractTileChunk oldChunk = chunks.get(chunkPos);
						AbstractTileChunk newChunk = oldChunk.upgrade(neighbours, worldSeed);
//						System.out.println("Upgraded chunk at " + chunkPos + ": " + oldChunk + " to " + newChunk);
						chunks.put(chunkPos, newChunk);
						if (newChunk.layer() == maxLayer) {
							TileChunk flc = (TileChunk) newChunk;
							flc.addTo(nextState);
						}
					}
				}
			}
		}
	}

	public WorldMap copy() {
		WorldMap copy = new WorldMap();
		copy.chunks = new HashMap<>(chunks);
		return copy;
	}

}
