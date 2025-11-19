package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;

public class IceTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected IceTile() {
	}

	public IceTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		int color1 = rgb(186, 215, 227);
		int color2 = rgb(170, 200, 215);
		this.color = (coord.x() + coord.y()) % 2 == 0 ? color1 : color2;
	}

}
