package com.nomadrealms.logic.game.world;

import static com.nomadrealms.logic.game.world.area.Tile.TILE_HORIZONTAL_SPACING;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_VERTICAL_SPACING;
import static com.nomadrealms.math.coordinate.map.ChunkCoordinate.CHUNK_SIZE;
import static com.nomadrealms.math.coordinate.map.RegionCoordinate.REGION_SIZE;
import static com.nomadrealms.math.coordinate.map.ZoneCoordinate.ZONE_SIZE;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.algorithm.generation.map.MapGenerationStrategy;
import com.nomadrealms.logic.game.world.area.Region;
import com.nomadrealms.math.coordinate.Coordinate;
import com.nomadrealms.math.coordinate.map.RegionCoordinate;

public class World {

	private final MapGenerationStrategy strategy;

	private final Map<RegionCoordinate, Region> regions = new HashMap<>();

	public World(MapGenerationStrategy strategy) {
		this.strategy = strategy;
	}

	public void render(Vector2 camera) {
		RegionCoordinate regionIndex = regionCoordOf(camera);
		Region region = regions.computeIfAbsent(regionIndex, (coord) -> new Region(strategy, this, coord));
		region.render(camera);
	}

	public void addRegion(RegionCoordinate coord) {
		regions.put(coord, new Region(strategy, this, coord));
	}

	private RegionCoordinate regionCoordOf(Vector2 position) {
		position = position.cpy().scl(1.0f / (REGION_SIZE * ZONE_SIZE * CHUNK_SIZE)).scl(1 / TILE_HORIZONTAL_SPACING,
				1 / TILE_VERTICAL_SPACING);
		return new RegionCoordinate((int) position.x, (int) position.y);
	}

	/**
	 * Returns the region at the given coordinates. If the region does not exist, it is generated.
	 *
	 * @param coord the coordinates
	 * @return the region
	 */
	public Region getRegion(Coordinate coord) {
		Region region = regions.get(coord.region());
		if (region == null) {
			region = new Region(strategy, this, coord.region());
			regions.put(coord.region(), region);
		}
		return region;
	}

}
