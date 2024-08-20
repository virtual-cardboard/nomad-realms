package nomadrealms.game.world.map.area;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;

import common.math.Vector2f;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;

/**
 * A chunk is a 16x16 grid of tiles. This is the optimal size for batch rendering.
 */
public class Chunk {

	private final Zone zone;
	private final ChunkCoordinate coord;

	private final Tile[][] tiles;

	public Chunk(Zone zone, ChunkCoordinate coord) {
		this.zone = zone;
		this.coord = coord;
		tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				tiles[x][y] = new Tile(this, new TileCoordinate(coord, x, y));
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
					coord.x() * TILE_HORIZONTAL_SPACING,
					coord.y() * TILE_VERTICAL_SPACING)
				.scale(CHUNK_SIZE);
	}

	public Vector2f pos() {
		return zone.pos().add(indexPosition());
	}

	public Tile getTile(TileCoordinate tile) {
		assert tile.chunk().equals(coord);
		return tiles[tile.x()][tile.y()];
	}

	public ChunkCoordinate coord() {
		return coord;
	}

	public void replace(Tile tile) {
		tiles[tile.coord().x()][tile.coord().y()] = tile;
	}

}
