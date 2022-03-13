package model.world.tile;

import model.timeline.VersionObject;

public class TileVersioned extends VersionObject<Tile> {

	public TileVersioned(Tile object) {
		super(object);
	}

	@Override
	public TileDiff diff(Tile other) {
		TileDiff diff = new TileDiff();
		return diff;
	}

}
