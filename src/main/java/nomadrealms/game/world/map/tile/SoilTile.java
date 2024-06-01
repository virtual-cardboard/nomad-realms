package nomadrealms.game.world.map.tile;

import nomadrealms.game.world.map.Chunk;

import static common.colour.Colour.rgb;

public class SoilTile extends Tile {

    public SoilTile(Chunk chunk, int x, int y) {
        super(chunk, x, y);
        color = rgb(106, 66, 45);
    }

}
