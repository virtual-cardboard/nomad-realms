package nomadrealms.game.world.map.tile;

import static engine.common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class StoneTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected StoneTile() {
	}

	public StoneTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(144, 152, 163);
	}

}
