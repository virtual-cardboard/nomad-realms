package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;

public class GrayscaleTile extends Tile {

	public GrayscaleTile(Chunk chunk, int x, int y, float grayscale) {
		super(chunk, x, y);
		color = rgb((int) (256 * grayscale), (int) (256 * grayscale), (int) (256 * grayscale));
	}

}
