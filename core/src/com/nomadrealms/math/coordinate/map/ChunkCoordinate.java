package com.nomadrealms.math.coordinate.map;

import static com.nomadrealms.math.coordinate.map.ZoneCoordinate.ZONE_SIZE;

import com.nomadrealms.math.coordinate.Coordinate;

public class ChunkCoordinate extends Coordinate {

	/**
	 * The size of a chunk in tiles.
	 */
	public static final int CHUNK_SIZE = 16;

	private ZoneCoordinate zone;

	public ChunkCoordinate(ZoneCoordinate zone, int x, int y) {
		super(x, y);
		this.zone = zone;
	}

	@Override
	public ChunkCoordinate up() {
		return new ChunkCoordinate(y() == ZONE_SIZE - 1 ? zone.up() : zone, x(), y() + 1);
	}

	@Override
	public ChunkCoordinate down() {
		return new ChunkCoordinate(y() == 0 ? zone.down() : zone, x(), y() - 1);
	}

	@Override
	public ChunkCoordinate left() {
		return new ChunkCoordinate(x() == 0 ? zone.left() : zone, x() - 1, y());
	}

	@Override
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

}
