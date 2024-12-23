package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class IceTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected IceTile() {
	}

	public IceTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(186, 215, 227);
	}

}
