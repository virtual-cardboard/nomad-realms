package com.nomadrealms.logic.game.world.area;

import static com.nomadrealms.logic.game.world.area.Chunk.CHUNK_SIZE;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_HORIZONTAL_SPACING;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_VERTICAL_SPACING;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.math.Vector2i;

public class Zone {

	public static final int ZONE_SIZE = 16;

	private Region region;
	private Vector2i index;

	private Chunk[][] chunks;

	public Zone(Vector2 pos) {
	}

	public Zone(Region region, int i, int j) {
		this.region = region;
		this.index = new Vector2i(i, j);
		chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunks[x][y] = new Chunk(this, x, y);
			}
		}
	}

	public void render(Vector2 camera) {
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunks[x][y].render(camera);
			}
		}
	}

	private Vector2 indexPosition() {
		return new Vector2(index.x, index.y).scl(ZONE_SIZE * CHUNK_SIZE).scl(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING);
	}

	public Vector2 pos() {
		return region.pos().add(indexPosition());
	}

}
