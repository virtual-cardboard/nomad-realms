package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;

public class GrayscaleTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected GrayscaleTile() {
	}

	public GrayscaleTile(Chunk chunk, TileCoordinate coord, float grayscale) {
		super(chunk, coord);
		float grayscale1 = grayscale;
		float grayscale2 = grayscale - 0.1f;
		float finalGrayscale = (coord.x() + coord.y()) % 2 == 0 ? grayscale1 : grayscale2;
		color = rgb((int) (256 * finalGrayscale), (int) (256 * finalGrayscale), (int) (256 * finalGrayscale));
	}

}
