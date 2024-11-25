package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class DeepWaterTile extends Tile {

	public DeepWaterTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgb(0, 0, 128);
	}

}
