package nomadrealms.game.world.map.area.coordinate;

import static java.lang.Math.floor;
import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import java.util.Objects;

import engine.common.math.Vector2f;
import engine.common.math.Vector2i;
import nomadrealms.game.world.map.area.coordinate.diff.RegionCoordinateDiff;

public class RegionCoordinate extends Coordinate {

	/**
	 * The size of a region in zones.
	 */
	public static final int REGION_SIZE = 3;

	/**
	 * No-arg constructor for serialization.
	 */
	protected RegionCoordinate() {
		this(0, 0);
	}

	public RegionCoordinate(int x, int y) {
		super(x, y);
	}

	public RegionCoordinate(Vector2i position) {
		this(position.x(), position.y());
	}

	public RegionCoordinate up() {
		return new RegionCoordinate(x(), y() - 1);
	}

	public RegionCoordinate down() {
		return new RegionCoordinate(x(), y() + 1);
	}

	public RegionCoordinate left() {
		return new RegionCoordinate(x() - 1, y());
	}

	public RegionCoordinate right() {
		return new RegionCoordinate(x() + 1, y());
	}

	@Override
	public RegionCoordinate region() {
		return this;
	}

	public static RegionCoordinate regionCoordinateOf(Vector2f position) {
		Vector2f tileToRegion = new Vector2f(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING)
				.scale(CHUNK_SIZE)
				.scale(ZONE_SIZE)
				.scale(REGION_SIZE);
		return new RegionCoordinate(
				(int) floor(position.x() / tileToRegion.x()),
				(int) floor(position.y() / tileToRegion.y()));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RegionCoordinate) {
			RegionCoordinate other = (RegionCoordinate) o;
			return x() == other.x() && y() == other.y();
		}
		return false;
	}

	public ZoneCoordinate[][] zoneCoordinates() {
		ZoneCoordinate[][] zones = new ZoneCoordinate[REGION_SIZE][REGION_SIZE];
		for (int x = 0; x < REGION_SIZE; x++) {
			for (int y = 0; y < REGION_SIZE; y++) {
				zones[x][y] = new ZoneCoordinate(this, x, y);
			}
		}
		return zones;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x(), y());
	}

	@Override
	public String toString() {
		return "Region(" + x() + "," + y() + ")";
	}

	public RegionCoordinateDiff sub(RegionCoordinate region) {
		return new RegionCoordinateDiff(x() - region.x(), y() - region.y());
	}

}
