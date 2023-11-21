package com.nomadrealms.logic.game.world.area;

import static com.nomadrealms.logic.game.world.area.Tile.TILE_HORIZONTAL_SPACING;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_VERTICAL_SPACING;
import static com.nomadrealms.math.coordinate.map.ChunkCoordinate.CHUNK_SIZE;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.math.Vector2i;

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

	public void render(Vector2 camera) {
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				tiles[x][y].render(camera);
			}
		}
	}

	private Vector2 indexPosition() {
		return new Vector2(index.x, index.y).scl(CHUNK_SIZE).scl(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING);
	}

	public Vector2 pos() {
		return zone.pos().add(indexPosition());
	}

}
