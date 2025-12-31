package nomadrealms.context.game.world.map.generation;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.GrassTile;

public class TestMapGenerationStrategy implements MapGenerationStrategy {

    @Override
    public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
        Tile[][] tiles = new Tile[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                tiles[i][j] = new GrassTile(chunk, new TileCoordinate(coord, i, j));
            }
        }
        return tiles;
    }

    @Override
    public Chunk[][] generateZone(World world, Zone zone) {
        Chunk[][] chunks = new Chunk[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                ChunkCoordinate chunkCoord = new ChunkCoordinate(zone.coord(), i, j);
                Chunk chunk = new Chunk(zone, chunkCoord);
                chunk.tiles(generateChunk(zone, chunk, chunkCoord));
                chunks[i][j] = chunk;
            }
        }
        return chunks;
    }

    @Override
    public long seed() {
        return 123456789L;
    }

}