package model.world;

import static context.game.visuals.GameCamera.RENDER_RADIUS;
import static model.world.TileChunk.chunkPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import math.WorldPos;
import model.state.GameState;
import model.world.layer.finallayer.FinalLayerChunk;
import model.world.layer.generatebiomes.GenerateBiomesChunk;

public class WorldMap {

	private Map<Vector2i, TileChunk> chunks = new HashMap<>();
	private long worldSeed = 0;
	private int maxLayer = new FinalLayerChunk(null).layer();

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

	public FinalLayerChunk finalLayerChunk(Vector2i chunkPos) {
		TileChunk chunk = chunk(chunkPos);
		return chunk instanceof FinalLayerChunk ? (FinalLayerChunk) chunk : null;
	}

	public TileChunk chunk(long tileID) {
		return chunk(chunkPos(tileID));
	}

	public FinalLayerChunk finalLayerChunk(long tileID) {
		return finalLayerChunk(chunkPos(tileID));
	}

	public void addChunk(TileChunk chunk) {
		chunks.put(chunk.pos(), chunk);
	}

	public Tile tile(WorldPos worldPos) {
		return finalLayerChunk(worldPos.chunkPos()).tile(worldPos.tilePos());
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
						TileChunk[][] neighbours = { { null, null, null }, { null, null, null }, { null, null, null } };
						for (int row = -1; row <= 1; row++) {
							for (int col = -1; col <= 1; col++) {
								neighbours[row + 1][col + 1] = chunks.get(chunkPos.add(col, row));
							}
						}
						TileChunk oldChunk = chunks.get(chunkPos);
						TileChunk newChunk = oldChunk.upgrade(neighbours, worldSeed);
//						System.out.println("Upgraded chunk at " + chunkPos + ": " + oldChunk + " to " + newChunk);
						chunks.put(chunkPos, newChunk);
						if (newChunk.layer() == maxLayer) {
							FinalLayerChunk flc = (FinalLayerChunk) newChunk;
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
