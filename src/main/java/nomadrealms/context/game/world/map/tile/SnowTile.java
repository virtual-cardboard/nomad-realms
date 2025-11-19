package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;
import static nomadrealms.context.game.world.map.tile.factory.TileType.SNOW;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class SnowTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected SnowTile() {
	}

	public SnowTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		int color1 = rgb(217, 217, 217);
		int color2 = rgb(200, 200, 200);
		this.color = (coord.x() + coord.y()) % 2 == 0 ? color1 : color2;
	}

	@Override
	public TileType type() {
		return SNOW;
	}

}
