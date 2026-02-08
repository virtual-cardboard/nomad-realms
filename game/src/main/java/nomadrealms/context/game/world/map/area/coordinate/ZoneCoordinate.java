package nomadrealms.context.game.world.map.area.coordinate;

import static engine.common.math.MathUtil.posMod;
import static java.lang.Math.floor;
import static java.util.Objects.hash;
import static nomadrealms.context.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate.regionCoordinateOf;

import engine.common.math.Vector2f;
import engine.common.math.Vector2i;
import nomadrealms.context.game.world.map.area.coordinate.diff.RegionCoordinateDiff;
import nomadrealms.context.game.world.map.area.coordinate.diff.ZoneCoordinateDiff;

public class ZoneCoordinate extends Coordinate {

	/**
	 * The size of a zone in chunks.
	 */
	public static final int ZONE_SIZE = 16;

	private final RegionCoordinate region;

	/**
	 * No-arg constructor for serialization.
	 */
	protected ZoneCoordinate() {
		this(null, 0, 0);
	}

	public ZoneCoordinate(RegionCoordinate region, int x, int y) {
		super(x, y);
		this.region = region;
	}

	public ZoneCoordinate(RegionCoordinate region, Vector2i position) {
		this(region, position.x(), position.y());
	}

	public ZoneCoordinate up() {
		return new ZoneCoordinate(y() == 0 ? region.up() : region, x(), posMod(y() - 1, REGION_SIZE));
	}

	public ZoneCoordinate down() {
		return new ZoneCoordinate(y() == REGION_SIZE - 1 ? region.down() : region, x(), posMod(y() + 1, REGION_SIZE));
	}

	public ZoneCoordinate left() {
		return new ZoneCoordinate(x() == 0 ? region.left() : region, posMod(x() - 1, REGION_SIZE), y());
	}

	public ZoneCoordinate right() {
		return new ZoneCoordinate(x() == REGION_SIZE - 1 ? region.right() : region, posMod(x() + 1, REGION_SIZE), y());
	}


	/**
	 * Generates the seed for the random number generator that is unique to this zone.
	 *
	 * @return the seed for the random number generator
	 */
	public long rngSeed(long worldSeed) {
		return hash(worldSeed, this);
	}

	@Override
	public RegionCoordinate region() {
		return region;
	}

	/**
	 * Run this if you want to fix coordinates that are out of bound instead of doing the tough math to prevent it from
	 * happening in the first place. Calling this is probably a bug waiting to happen.
	 */
	public ZoneCoordinate normalize() {
		int x = posMod(x(), REGION_SIZE);
		int y = posMod(y(), REGION_SIZE);
		RegionCoordinate regionCoord = region;
		if (x() < 0) {
			regionCoord = regionCoord.left();
		} else if (x() >= REGION_SIZE) {
			regionCoord = regionCoord.right();
		}
		if (y() < 0) {
			regionCoord = regionCoord.up();
		} else if (y() >= REGION_SIZE) {
			regionCoord = regionCoord.down();
		}
		return new ZoneCoordinate(regionCoord, x, y);
	}

	public static ZoneCoordinate zoneCoordinateOf(Vector2f position) {
		RegionCoordinate regionCoord = regionCoordinateOf(position);
		Vector2f offset = new Vector2f()
				.add(new Vector2f(regionCoord.x(), regionCoord.y())).scale(REGION_SIZE)
				.scale(ZONE_SIZE)
				.scale(CHUNK_SIZE)
				.scale(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING)
				.sub(position).negate();
		Vector2f tileToZone = new Vector2f(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING)
				.scale(CHUNK_SIZE)
				.scale(ZONE_SIZE);
		return new ZoneCoordinate(regionCoord,
				(int) floor(offset.x() / tileToZone.x()),
				(int) floor(offset.y() / tileToZone.y()))
				.normalize();
	}

	public boolean equals(Object o) {
		if (o instanceof ZoneCoordinate) {
			ZoneCoordinate other = (ZoneCoordinate) o;
			return x() == other.x() && y() == other.y() && region.equals(other.region);
		}
		return false;
	}

	public ChunkCoordinate[][] chunkCoordinates() {
		ChunkCoordinate[][] chunkCoords = new ChunkCoordinate[ZONE_SIZE][ZONE_SIZE];
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunkCoords[x][y] = new ChunkCoordinate(this, x, y);
			}
		}
		return chunkCoords;
	}

	@Override
	public String toString() {
		return region.toString() + ".Zone(" + x() + "," + y() + ")";
	}

	public ZoneCoordinateDiff sub(ZoneCoordinate zone) {
		RegionCoordinateDiff regionDiff = region.sub(zone.region());
		return new ZoneCoordinateDiff(regionDiff, x() - zone.x(), y() - zone.y());
	}

}
