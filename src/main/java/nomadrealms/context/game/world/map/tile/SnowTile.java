package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;

public class SnowTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected SnowTile() {
	}

	public SnowTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(217, 217, 217);
	}

}
