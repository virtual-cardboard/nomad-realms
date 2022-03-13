package model.world.chunk;

import static model.world.chunk.AbstractTileChunk.CHUNK_SIDE_LENGTH;

import model.timeline.VersionObject;

public class TileChunkVersioned extends VersionObject<TileChunk> {

	public TileChunkVersioned(TileChunk object) {
		super(object);
	}

	@Override
	public TileChunkDiff diff(TileChunk other) {
		TileChunkDiff diff = new TileChunkDiff();
		for (int i = 0; i < CHUNK_SIDE_LENGTH; i++) {
			for (int j = 0; j < CHUNK_SIDE_LENGTH; j++) {
//				if(object.tile(i, j))) {
//					
//				}
			}
		}
		return diff;
	}

}
