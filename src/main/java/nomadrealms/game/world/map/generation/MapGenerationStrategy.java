package nomadrealms.game.world.map.generation;

import common.math.Vector2i;
import nomadrealms.game.world.map.Chunk;
import nomadrealms.game.world.map.tile.Tile;

public abstract class MapGenerationStrategy {

    public abstract Tile[][] generate(Chunk chunk, Vector2i coord);

}
