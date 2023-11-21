package com.nomadrealms.logic.game.world.area;

import static com.nomadrealms.logic.game.world.area.Tile.TILE_HORIZONTAL_SPACING;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_VERTICAL_SPACING;
import static com.nomadrealms.math.coordinate.map.ChunkCoordinate.CHUNK_SIZE;
import static com.nomadrealms.math.coordinate.map.RegionCoordinate.REGION_SIZE;
import static com.nomadrealms.math.coordinate.map.ZoneCoordinate.ZONE_SIZE;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.algorithm.generation.map.MapGenerationStrategy;
import com.nomadrealms.logic.game.world.World;
import com.nomadrealms.math.Vector2i;
import com.nomadrealms.math.coordinate.map.RegionCoordinate;
import com.nomadrealms.math.coordinate.map.ZoneCoordinate;

public class Region {

	private long timestamp;
	private MapGenerationStrategy strategy;
	private World world;

	private final RegionCoordinate coord;

	private final Zone[][] zones;

	public Region(MapGenerationStrategy strategy, World world, RegionCoordinate coord) {
		this.strategy = strategy;
		this.world = world;
		this.coord = coord;
		zones = new Zone[REGION_SIZE][REGION_SIZE];
	}

	public void render(Vector2 camera) {
		Vector2i zoneIndex = zoneIndexOf(camera);
		if (zones[zoneIndex.x][zoneIndex.y] == null) {
			zones[zoneIndex.x][zoneIndex.y] = strategy.generateZone(world, new ZoneCoordinate(coord, zoneIndex.x,
					zoneIndex.y));
		}
		zones[zoneIndex.x][zoneIndex.y].render(camera);
	}

	private Vector2i zoneIndexOf(Vector2 position) {
		float tileToZone = ZONE_SIZE * CHUNK_SIZE;
		float tileToRegion = REGION_SIZE * tileToZone;
		Vector2 offset = new Vector2(position.x - tileToRegion * coord.x(), position.y - tileToRegion * coord.y());
		return new Vector2i((int) (offset.x / tileToZone), (int) (offset.y / tileToZone));
	}

	private Vector2 indexPosition() {
		return new Vector2(coord.x(), coord.y()).scl(REGION_SIZE * ZONE_SIZE * CHUNK_SIZE).scl(TILE_HORIZONTAL_SPACING,
				TILE_VERTICAL_SPACING);
	}

	public Vector2 pos() {
		return indexPosition();
	}

	public boolean zone(int x, int y) {
		return false;
	}

	public RegionCoordinate coord() {
		return coord;
	}

}
