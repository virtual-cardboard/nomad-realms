package context.game.logic.handler;

import static model.world.chunk.AbstractTileChunk.CHUNK_SIDE_LENGTH;

import java.util.List;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import engine.common.math.Vector2i;
import event.network.p2p.game.StreamChunkDataEvent;
import model.world.chunk.TileChunk;
import model.world.tile.Tile;
import model.world.tile.TileType;

/**
 * Handles the {@link StreamChunkDataEvent} event.
 */
public class StreamChunkDataEventHandler implements Consumer<StreamChunkDataEvent> {

	private final NomadsGameData data;

	public StreamChunkDataEventHandler(NomadsGameData data) {
		this.data = data;
	}

	/**
	 * Sets the chunk data to be the data received in the event.
	 *
	 * @param e The StreamChunkDataEvent to handle.
	 */
	@Override
	public void accept(StreamChunkDataEvent e) {
		Vector2i chunkPos = new Vector2i(e.cx(), e.cy());
		List<Integer> tileTypes = e.tileTypes();
		TileChunk chunk = data.currentState().worldMap().chunk(chunkPos);

		if (chunk == null) {
			chunk = new TileChunk(chunkPos);
			data.currentState().worldMap().addChunk(chunk);
		}

		Tile[][] tiles = new Tile[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
//				tiles[x][y] = new Tile(x, y, TileType.values()[tileTypes.get(y * CHUNK_SIDE_LENGTH + x)], chunk);
				// Set the tile to be a GRASS tile
				tiles[x][y] = new Tile(x, y, TileType.RICH_GRASS, chunk);
			}
		}

		// Replace the tiles in the chunk with the new ones
		chunk.setTiles(tiles);

		data.tools().logMessage("Received chunk " + chunkPos + " from " + e.source());
	}

}
