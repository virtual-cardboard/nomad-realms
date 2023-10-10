package com.nomadrealms.logic.game.world.area;

import static com.nomadrealms.logic.game.world.area.Chunk.CHUNK_SIZE;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_HORIZONTAL_SPACING;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_VERTICAL_SPACING;
import static com.nomadrealms.logic.game.world.area.Zone.ZONE_SIZE;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.math.Vector2i;

public class Region {

	public static final int REGION_SIZE = 3;

	private Vector2i index;

	private Zone[][] zones;

	public Region(Vector2i index) {
		this.index = index;
		zones = new Zone[REGION_SIZE][REGION_SIZE];
		for (int x = 0; x < REGION_SIZE; x++) {
			for (int y = 0; y < REGION_SIZE; y++) {
				zones[x][y] = new Zone(this, x, y);
			}
		}
	}

	public void render(Vector2 camera) {
		Vector2i zoneIndex = zoneIndexOf(camera);
		zones[zoneIndex.x][zoneIndex.y].render(camera);
	}

	private Vector2i zoneIndexOf(Vector2 position) {
		float tileToZone = ZONE_SIZE * CHUNK_SIZE;
		float tileToRegion = REGION_SIZE * tileToZone;
		Vector2 offset = new Vector2(position.x - tileToRegion * index.x, position.y - tileToRegion * index.y);
		return new Vector2i((int) (offset.x / tileToZone), (int) (offset.y / tileToZone));
	}

	private Vector2 indexPosition() {
		return new Vector2(index.x, index.y).scl(REGION_SIZE * ZONE_SIZE * CHUNK_SIZE).scl(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING);
	}

	public Vector2 pos() {
		return indexPosition();
	}

}
