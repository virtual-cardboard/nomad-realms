package nomadrealms.game.world.map.tile;

import static engine.common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class WaterTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected WaterTile() {
	}

	public WaterTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(116, 204, 244);
	}

	public WaterTile(Chunk chunk, TileCoordinate coord, int rgb) {
		super(chunk, coord);
		color = rgb;
	}

}
