package model.world.chunk;

import model.timeline.VersionDiff;
import model.timeline.VersionObject;

/**
 * Sparse representation of chunk diffs.
 * 
 * @author Lunkle
 *
 */
public class TileChunkDiff implements VersionDiff<TileChunk> {

	@Override
	public VersionObject<TileChunk> applyTo(VersionObject<TileChunk> object) {
		return null;
	}

}
