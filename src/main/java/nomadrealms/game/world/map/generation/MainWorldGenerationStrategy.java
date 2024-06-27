package nomadrealms.game.world.map.generation;

import common.math.Vector2i;
import nomadrealms.game.world.map.Chunk;
import nomadrealms.game.world.map.tile.Tile;

public class MainWorldGenerationStrategy extends MapGenerationStrategy {


    @Override
    public Tile[][] generate(Chunk chunk, Vector2i coord) {
        return new Tile[0][];
    }

}
