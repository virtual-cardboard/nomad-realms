package nomadrealms.game.world.map.tile;

import nomadrealms.game.world.map.Chunk;

import static common.colour.Colour.rgb;

public class WaterTile extends Tile {

	public WaterTile(Chunk chunk, int x, int y) {
		super(chunk, x, y);
		color = rgb(116,204,244);
	}

}
