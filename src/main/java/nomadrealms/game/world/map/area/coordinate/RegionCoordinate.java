package nomadrealms.game.world.map.area.coordinate;

public class RegionCoordinate extends Coordinate {

	/**
	 * The size of a region in zones.
	 */
	public static final int REGION_SIZE = 3;

	public RegionCoordinate(int x, int y) {
		super(x, y);
	}

	public RegionCoordinate up() {
		return new RegionCoordinate(x(), y() + 1);
	}

	public RegionCoordinate down() {
		return new RegionCoordinate(x(), y() - 1);
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

	public boolean equals(Object o) {
		if (o instanceof RegionCoordinate) {
			RegionCoordinate other = (RegionCoordinate) o;
			return x() == other.x() && y() == other.y();
		}
		return false;
	}

}
