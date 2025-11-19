package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.b;
import static engine.common.colour.Colour.g;
import static engine.common.colour.Colour.r;
import static engine.common.colour.Colour.rgb;
import static nomadrealms.context.game.world.map.tile.factory.TileType.WATER;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class WaterTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected WaterTile() {
	}

	public WaterTile(Chunk chunk, TileCoordinate coord) {
		this(chunk, coord, rgb(116, 204, 244));
	}

	public WaterTile(Chunk chunk, TileCoordinate coord, int rgb) {
		super(chunk, coord);
		int alt = rgb((int) (r(rgb) * 0.9f), (int) (g(rgb) * 0.9f), (int) (b(rgb) * 0.9f));
		this.color = (coord.x() + coord.y()) % 2 == 0 ? rgb : alt;
	}

	@Override
	public TileType type() {
		return WATER;
	}

}
