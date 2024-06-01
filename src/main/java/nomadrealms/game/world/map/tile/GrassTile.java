package nomadrealms.game.world.map.tile;

import nomadrealms.game.world.map.Chunk;

import static common.colour.Colour.rgb;

public class GrassTile extends Tile {

	public GrassTile(Chunk chunk, int x, int y) {
		super(chunk, x, y);
		color = rgb(126, 200, 80);
	}

}
