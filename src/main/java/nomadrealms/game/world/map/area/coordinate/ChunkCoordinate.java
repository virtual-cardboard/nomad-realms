package nomadrealms.game.world.map.area.coordinate;

import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

public class ChunkCoordinate extends Coordinate {

	/**
	 * The size of a chunk in tiles.
	 */
	public static final int CHUNK_SIZE = 16;

	private final ZoneCoordinate zone;

	public ChunkCoordinate(ZoneCoordinate zone, int x, int y) {
		super(x, y);
		this.zone = zone;
	}

	public ChunkCoordinate up() {
		return new ChunkCoordinate(y() == 0 ? zone.up() : zone, x(), y() - 1);
	}

	public ChunkCoordinate down() {
		return new ChunkCoordinate(y() == ZONE_SIZE - 1 ? zone.down() : zone, x(), y() + 1);
	}

	public ChunkCoordinate left() {
		return new ChunkCoordinate(x() == 0 ? zone.left() : zone, x() - 1, y());
	}

	public ChunkCoordinate right() {
		return new ChunkCoordinate(x() == ZONE_SIZE - 1 ? zone.right() : zone, x() + 1, y());
	}

	@Override
	public RegionCoordinate region() {
		return zone.region();
	}

	public ZoneCoordinate zone() {
		return zone;
	}

	public boolean equals(Object o) {
		if (o instanceof ChunkCoordinate) {
			ChunkCoordinate other = (ChunkCoordinate) o;
			return x() == other.x() && y() == other.y() && zone.equals(other.zone);
		}
		return false;
	}

}
