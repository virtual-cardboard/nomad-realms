package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;

public class WaterTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected WaterTile() {
	}

	public WaterTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		int color1 = rgb(116, 204, 244);
		int color2 = rgb(100, 190, 230);
		this.color = (coord.x() + coord.y()) % 2 == 0 ? color1 : color2;
	}

	public WaterTile(Chunk chunk, TileCoordinate coord, int rgb) {
		super(chunk, coord);
		color = rgb;
	}

}
