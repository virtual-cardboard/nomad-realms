package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;

public class StoneTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected StoneTile() {
	}

	public StoneTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		int color1 = rgb(144, 152, 163);
		int color2 = rgb(130, 140, 150);
		this.color = (coord.x() + coord.y()) % 2 == 0 ? color1 : color2;
	}

}
