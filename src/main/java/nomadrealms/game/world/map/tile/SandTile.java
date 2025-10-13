package nomadrealms.game.world.map.tile;

import static engine.common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class SandTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected SandTile() {
	}

	public SandTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(194, 178, 128);
	}

}
