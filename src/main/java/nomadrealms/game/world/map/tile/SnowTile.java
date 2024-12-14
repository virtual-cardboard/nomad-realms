package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class SnowTile extends Tile {

	public SnowTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(217, 217, 217);
	}

}
