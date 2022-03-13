package model.world.tile;

import model.timeline.VersionDiff;
import model.timeline.VersionObject;

/**
 * Sparse representation of chunk diffs.
 * 
 * @author Lunkle
 *
 */
public class TileDiff implements VersionDiff<Tile> {

	@Override
	public VersionObject<Tile> applyTo(VersionObject<Tile> object) {
		return null;
	}

}
