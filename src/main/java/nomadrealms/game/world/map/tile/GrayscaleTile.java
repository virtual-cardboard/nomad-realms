package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class GrayscaleTile extends Tile {

	public GrayscaleTile(Chunk chunk, TileCoordinate coord, float grayscale) {
		super(chunk, coord);
		color = rgb((int) (256 * grayscale), (int) (256 * grayscale), (int) (256 * grayscale));
	}

}
