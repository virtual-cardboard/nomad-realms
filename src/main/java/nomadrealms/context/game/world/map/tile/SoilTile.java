package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;
import static nomadrealms.context.game.world.map.tile.factory.TileType.SOIL;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class SoilTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected SoilTile() {
	}

	public SoilTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		int color1 = rgb(106, 66, 45);
		int color2 = rgb(90, 50, 30);
		this.color = (coord.x() + coord.y()) % 2 == 0 ? color1 : color2;
	}

	@Override
	public TileType type() {
		return SOIL;
	}
	
}
