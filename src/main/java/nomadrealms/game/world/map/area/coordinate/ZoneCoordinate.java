package nomadrealms.game.world.map.area.coordinate;

import static java.lang.Math.floor;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.regionCoordinateOf;

import common.math.Vector2f;
import nomadrealms.game.world.map.area.Region;

public class ZoneCoordinate extends Coordinate {

	/**
	 * The size of a zone in chunks.
	 */
	public static final int ZONE_SIZE = 16;

	private final RegionCoordinate region;

	public ZoneCoordinate(RegionCoordinate region, int x, int y) {
		super(x, y);
		this.region = region;
	}

	private ZoneCoordinate(Region region, int x, int y) {
		this(region.coord(), x, y);
	}

	public ZoneCoordinate up() {
		return new ZoneCoordinate(y() == REGION_SIZE - 1 ? region.up() : region, x(), y() + 1);
	}

	public ZoneCoordinate down() {
		return new ZoneCoordinate(y() == 0 ? region.down() : region, x(), y() - 1);
	}

	public ZoneCoordinate left() {
		return new ZoneCoordinate(x() == 0 ? region.left() : region, x() - 1, y());
	}

	public ZoneCoordinate right() {
		return new ZoneCoordinate(x() == REGION_SIZE - 1 ? region.right() : region, x() + 1, y());
	}

	public static ZoneCoordinate zoneIndexOf(Vector2f position) {
		RegionCoordinate regionCoord = regionCoordinateOf(position);
		float tileToZone = ZONE_SIZE * CHUNK_SIZE;
		float tileToRegion = REGION_SIZE * tileToZone;
		Vector2f offset = new Vector2f(
				position.x() - tileToRegion * regionCoord.x(),
				position.y() - tileToRegion * regionCoord.y());
		return new ZoneCoordinate(regionCoord,
				(int) floor(offset.x() / tileToZone),
				(int) floor(offset.y() / tileToZone));
	}

	@Override
	public RegionCoordinate region() {
		return region;
	}

	public boolean equals(Object o) {
		if (o instanceof ZoneCoordinate) {
			ZoneCoordinate other = (ZoneCoordinate) o;
			return x() == other.x() && y() == other.y() && region.equals(other.region);
		}
		return false;
	}

}
