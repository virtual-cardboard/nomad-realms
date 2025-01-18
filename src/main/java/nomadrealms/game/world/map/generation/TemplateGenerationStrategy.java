package nomadrealms.game.world.map.generation;

import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.game.world.map.tile.factory.TileType.GRASS;
import static nomadrealms.game.world.map.tile.factory.TileType.WATER;

import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.tile.factory.TileFactory;
import nomadrealms.game.world.map.tile.factory.TileType;

public class TemplateGenerationStrategy implements MapGenerationStrategy {

    @Override
    public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
        return TileFactory.createTiles(chunk, new TileType[][] {
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, WATER, WATER, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS,
                        GRASS, GRASS },
                { GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS,
                        GRASS, GRASS },
        });
    }

    @Override
    public Chunk[][] generateZone(World world, Zone zone) {
        Chunk[][] chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
        for (int x = 0; x < ZONE_SIZE; x++) {
            for (int y = 0; y < ZONE_SIZE; y++) {
                ChunkCoordinate chunkCoord = new ChunkCoordinate(zone.coord(), x, y);
                chunks[x][y] = new Chunk(zone, chunkCoord);
                chunks[x][y].tiles(generateChunk(zone, chunks[x][y], chunkCoord));
            }
        }
        return chunks;
    }

}