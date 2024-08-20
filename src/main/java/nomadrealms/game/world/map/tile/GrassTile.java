package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class GrassTile extends Tile {

	public GrassTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(126, 200, 80);
	}

}
