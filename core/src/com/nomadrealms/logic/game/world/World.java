package com.nomadrealms.logic.game.world;

import static com.nomadrealms.logic.game.world.area.Chunk.CHUNK_SIZE;
import static com.nomadrealms.logic.game.world.area.Region.REGION_SIZE;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_HORIZONTAL_SPACING;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_VERTICAL_SPACING;
import static com.nomadrealms.logic.game.world.area.Zone.ZONE_SIZE;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.logic.game.world.area.Region;
import com.nomadrealms.math.Vector2i;

public class World {

	private Map<Vector2i, Region> regions = new HashMap<>();

	public void render(Vector2 camera) {
		Vector2i regionIndex = regionIndexOf(camera);
		Region region = regions.get(regionIndex);
		region.render(camera);
	}

	public void addRegion(Vector2i vector) {
		regions.put(vector, new Region(vector));
	}

	private Vector2i regionIndexOf(Vector2 position) {
		position = position.cpy().scl(1.0f / (REGION_SIZE * ZONE_SIZE * CHUNK_SIZE)).scl(1 / TILE_HORIZONTAL_SPACING,
				1 / TILE_VERTICAL_SPACING);
		return new Vector2i((int) position.x, (int) position.y);
	}

}
