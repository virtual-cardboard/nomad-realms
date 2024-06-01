package nomadrealms.game.world.map;

import nomadrealms.game.world.map.tile.factory.TileType;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.game.world.map.tile.factory.TileFactory;

public class Chunk {

	private Tile[][] tiles;

	public Chunk() {
		tiles = TileFactory.createTiles(this, new TileType[][] {
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
				{ TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.WATER, TileType.GRASS, TileType.GRASS, TileType.GRASS, TileType.GRASS },
		});
	}

	public void render(RenderingEnvironment renderingEnvironment) {
		for (int row = 0; row < 16; row++) {
			for (int col = 0; col < 16; col++) {
				tiles[row][col].render(renderingEnvironment);
			}
		}
	}

	public Tile getTile(int row, int col) {
		if (row < 0 || row >= 16 || col < 0 || col >= 16) {
			return null;
		}
		return tiles[row][col];
	}

    public void setTile(Tile tile) {
		tiles[tile.y()][tile.x()] = tile;
    }
}
