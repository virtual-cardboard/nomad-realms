package nomadrealms.game.world.map.area.coordinate.diff;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import common.math.Vector2f;

public class RegionCoordinateDiff {

	private final int x;
	private final int y;

	public RegionCoordinateDiff(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public Vector2f toVector2f() {
		return new Vector2f(x, y)
				.scale(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING)
				.scale(CHUNK_SIZE)
				.scale(ZONE_SIZE)
				.scale(REGION_SIZE);
	}

	@Override
	public String toString() {
		return "RegionCoordinateDiff [x=" + x + ", y=" + y + "]";
	}

}
