package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;

public class GrassTile extends Tile {

	public GrassTile(Chunk chunk, int x, int y) {
		super(chunk, x, y);
		color = rgb(126, 200, 80);
	}

}
