package nomadrealms.game.world.map.tile;

import nomadrealms.game.world.map.Chunk;

import static common.colour.Colour.rgb;

public class GrayscaleTile extends Tile {

	public GrayscaleTile(Chunk chunk, int x, int y, float grayscale) {
		super(chunk, x, y);
		color = rgb((int) (256 * grayscale), (int) (256 * grayscale), (int) (256 * grayscale));
	}

}
