package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class WaterTile extends Tile {

	public WaterTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(116,204,244);
	}

}
