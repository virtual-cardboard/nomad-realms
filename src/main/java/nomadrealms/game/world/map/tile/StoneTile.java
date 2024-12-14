package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class StoneTile extends Tile {

	public StoneTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(144,152,163);
	}

}
