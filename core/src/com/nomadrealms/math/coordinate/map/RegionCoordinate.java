package com.nomadrealms.math.coordinate.map;

import com.nomadrealms.math.coordinate.Coordinate;

public class RegionCoordinate extends Coordinate {

	/**
	 * The size of a region in zones.
	 */
	public static final int REGION_SIZE = 3;

	public RegionCoordinate(int x, int y) {
		super(x, y);
	}

	@Override
	public RegionCoordinate up() {
		return new RegionCoordinate(x(), y() + 1);
	}

	@Override
	public RegionCoordinate down() {
		return new RegionCoordinate(x(), y() - 1);
	}

	@Override
	public RegionCoordinate left() {
		return new RegionCoordinate(x() - 1, y());
	}

	@Override
	public RegionCoordinate right() {
		return new RegionCoordinate(x() + 1, y());
	}

}
