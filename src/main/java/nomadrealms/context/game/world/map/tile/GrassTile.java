package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;
import static nomadrealms.context.game.world.map.tile.factory.TileType.GRASS;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class GrassTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected GrassTile() {
	}

	public GrassTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		int color1 = rgb(126, 200, 80);
		int color2 = rgb(110, 180, 70);
		this.color = (coord.x() + coord.y()) % 2 == 0 ? color1 : color2;
	}

	public GrassTile(Chunk chunk, TileCoordinate coord, int color) {
		super(chunk, coord);
		this.color = color;
	}

	@Override
	public TileType type() {
		return GRASS;
	}

}
