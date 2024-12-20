package nomadrealms.game.world.map.area.coordinate;

import static java.lang.Math.floor;
import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import java.util.Objects;

import common.math.Vector2f;
import common.math.Vector2i;

public class RegionCoordinate extends Coordinate {

	/**
	 * The size of a region in zones.
	 */
	public static final int REGION_SIZE = 3;

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

	@Override
	public int hashCode() {
		return Objects.hash(x(), y());
	}

	@Override
	public String toString() {
		return "RegionCoordinate(" + x() + ", " + y() + ")";
	}

}
