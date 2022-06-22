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

public class StreamChunkDataEventHandler implements Consumer<StreamChunkDataEvent> {

	private final NomadsGameData data;

	public StreamChunkDataEventHandler(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public void accept(StreamChunkDataEvent e) {
		Vector2i chunkPos = new Vector2i(e.cx(), e.cy());
		List<Integer> tileTypes = e.tileTypes();
		TileChunk chunk = data.previousState().worldMap().chunk(chunkPos);
		Tile[][] tiles = new Tile[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
				tiles[x][y] = new Tile(x, y, TileType.values()[tileTypes.get(y * CHUNK_SIDE_LENGTH + x)], chunk);
			}
		}
		if (chunk != null) {
			chunk.setTiles(tiles);
		}
	}

}
