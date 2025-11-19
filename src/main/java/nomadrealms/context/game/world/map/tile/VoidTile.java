package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgba;
import static nomadrealms.context.game.world.map.tile.factory.TileType.VOID;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.render.RenderingEnvironment;

public class VoidTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected VoidTile() {
	}

	public VoidTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		color = rgba(0, 0, 0, 0);
	}

	@Override
	public void render(RenderingEnvironment re) {
		// Void tiles are not rendered.
	}

}
