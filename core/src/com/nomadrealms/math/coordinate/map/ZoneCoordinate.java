package com.nomadrealms.math.coordinate.map;

import static com.nomadrealms.math.coordinate.map.RegionCoordinate.REGION_SIZE;

import com.nomadrealms.math.coordinate.Coordinate;

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

	@Override
	public ZoneCoordinate up() {
		return new ZoneCoordinate(y() == REGION_SIZE - 1 ? region.up() : region, x(), y() + 1);
	}

	@Override
	public ZoneCoordinate down() {
		return new ZoneCoordinate(y() == 0 ? region.down() : region, x(), y() - 1);
	}

	@Override
	public ZoneCoordinate left() {
		return new ZoneCoordinate(x() == 0 ? region.left() : region, x() - 1, y());
	}

	@Override
	public ZoneCoordinate right() {
		return new ZoneCoordinate(x() == REGION_SIZE - 1 ? region.right() : region, x() + 1, y());
	}

}
