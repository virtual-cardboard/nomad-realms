package nomadrealms.game.world.map.generation;

import common.math.Vector2i;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.world.map.tile.factory.TileFactory;
import nomadrealms.game.world.map.tile.factory.TileType;

import static nomadrealms.game.world.map.tile.factory.TileType.GRASS;
import static nomadrealms.game.world.map.tile.factory.TileType.WATER;

public class TemplateGenerationStrategy implements MapGenerationStrategy {

    @Override
    public Tile[][] generate(Chunk chunk, Vector2i coord) {
        return TileFactory.createTiles(chunk, new TileType[][] {
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, WATER, WATER, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS },
        });
    }

    @Override
    public Zone generateZone(World world, ZoneCoordinate zoneCoordinate) {
        return null;
    }

}