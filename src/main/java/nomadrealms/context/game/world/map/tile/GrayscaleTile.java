package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;
import static nomadrealms.context.game.world.map.tile.factory.TileType.GRAYSCALE;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class GrayscaleTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected GrayscaleTile() {
	}

	public GrayscaleTile(Chunk chunk, TileCoordinate coord, float grayscale) {
		super(chunk, coord);
		color = rgb((int) (256 * grayscale), (int) (256 * grayscale), (int) (256 * grayscale));
	}

	@Override
	public TileType type() {
		return GRAYSCALE;
	}

}
