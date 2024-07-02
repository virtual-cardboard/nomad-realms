package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;

public class WaterTile extends Tile {

	public WaterTile(Chunk chunk, int x, int y) {
		super(chunk, x, y);
		color = rgb(116,204,244);
	}

}
