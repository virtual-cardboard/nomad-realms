package nomadrealms.game.world.map.tile;

import static engine.common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class GrassTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected GrassTile() {
	}

	public GrassTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(126, 200, 80);
	}

	public GrassTile(Chunk chunk, TileCoordinate coord, int color) {
		super(chunk, coord);
		this.color = color;
	}

}
