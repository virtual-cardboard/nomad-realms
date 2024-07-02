package nomadrealms.game.world.map.tile.factory;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.tile.GrassTile;
import nomadrealms.game.world.map.tile.SoilTile;
import nomadrealms.game.world.map.tile.WaterTile;

public class TileFactory {

    public static Tile[][] createTiles(Chunk chunk, TileType[][] tileTypes) {
        Tile[][] tiles = new Tile[16][16];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                tiles[x][y] = createTile(tileTypes[x][y], chunk, x, y);
            }
        }
        return tiles;
    }

    public static Tile createTile(TileType type, Chunk chunk, int x, int y) {
        switch (type) {
            case GRASS:
                return new GrassTile(chunk, x, y);
            case WATER:
                return new WaterTile(chunk, x, y);
            case SOIL:
                return new SoilTile(chunk, x, y);
            default:
                throw new RuntimeException("No tile case in TileFactory for tile type " + type);
        }
    }

}
