package nomadrealms.game.world.map;

import common.math.Vector2i;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.game.world.map.tile.factory.TileFactory;
import nomadrealms.game.world.map.tile.factory.TileType;
import nomadrealms.render.RenderingEnvironment;

import static nomadrealms.game.world.map.tile.factory.TileType.*;
import static nomadrealms.game.world.map.tile.factory.TileType.GRASS;

public class Chunk {

	public static int CHUNK_SIZE = 16;

	private final Vector2i coord;
	private Tile[][] tiles;

	public Chunk(Vector2i coord) {
		this.coord = coord;
		tiles = TileFactory.createTiles(this, new TileType[][] {
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

	public void render(RenderingEnvironment renderingEnvironment) {
		for (int row = 0; row < CHUNK_SIZE; row++) {
			for (int col = 0; col < CHUNK_SIZE; col++) {
				tiles[row][col].render(renderingEnvironment);
			}
		}
	}

	public Tile getTile(int col, int row) {
		if (row < 0 || row >= CHUNK_SIZE || col < 0 || col >= CHUNK_SIZE) {
			return null;
		}
		return tiles[row][col];
	}

    public void setTile(Tile tile) {
		tiles[tile.y()][tile.x()] = tile;
    }

	public int x() {
		return coord.x();
	}

	public int y() {
		return coord.y();
	}
}
