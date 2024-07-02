package nomadrealms.game.world.map.area;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;

import common.math.Vector2f;
import common.math.Vector2i;
import nomadrealms.render.RenderingEnvironment;

/**
 * A chunk is a 16x16 grid of tiles. This is the optimal size for batch rendering.
 */
public class Chunk {

	private Zone zone;
	private Vector2i index;

	private Tile[][] tiles;

	public Chunk(Zone zone, int i, int j) {
		this.zone = zone;
		this.index = new Vector2i(i, j);
		tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				tiles[x][y] = new Tile(this, x, y);
			}
		}
	}

	public Tile tile(int x, int y) {
		return tiles[x][y];
	}

	public void render(RenderingEnvironment re) {
		for (int row = 0; row < CHUNK_SIZE; row++) {
			for (int col = 0; col < CHUNK_SIZE; col++) {
				tiles[row][col].render(re);
			}
		}
	}

	private Vector2f indexPosition() {
		return new Vector2f(
					index.x() * TILE_HORIZONTAL_SPACING,
					index.y() * TILE_VERTICAL_SPACING)
				.scale(CHUNK_SIZE);
	}

	public Vector2f pos() {
		return zone.pos().add(indexPosition());
	}

}
