package nomadrealms.game.world.map.area.coordinate.diff;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import engine.common.math.Vector2f;

public class ZoneCoordinateDiff {

	private final RegionCoordinateDiff regionDiff;
	private final int x;
	private final int y;

	public ZoneCoordinateDiff(RegionCoordinateDiff regionDiff, int x, int y) {
		this.regionDiff = regionDiff;
		this.x = x;
		this.y = y;
	}

	public RegionCoordinateDiff regionDiff() {
		return regionDiff;
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
				.add(regionDiff.toVector2f());
	}

	@Override
	public String toString() {
		return "ZoneCoordinateDiff [x=" + x + ", y=" + y + "]";
	}

}
