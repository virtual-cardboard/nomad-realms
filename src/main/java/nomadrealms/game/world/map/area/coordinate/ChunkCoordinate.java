package nomadrealms.game.world.map.area.coordinate;

import static engine.common.math.MathUtil.posMod;
import static java.lang.Math.floor;
import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.zoneCoordinateOf;

import engine.common.math.Vector2f;
import engine.common.math.Vector2i;
import nomadrealms.game.world.map.area.coordinate.diff.ChunkCoordinateDiff;
import nomadrealms.game.world.map.area.coordinate.diff.ZoneCoordinateDiff;

public class ChunkCoordinate extends Coordinate {

	/**
	 * The size of a chunk in tiles.
	 */
	public static final int CHUNK_SIZE = 16;

	private final ZoneCoordinate zone;

	/**
	 * No-arg constructor for serialization.
	 */
	protected ChunkCoordinate() {
		this(null, 0, 0);
	}

	public ChunkCoordinate(ZoneCoordinate zone, int x, int y) {
		super(x, y);
		this.zone = zone;
	}

	public ChunkCoordinate(ZoneCoordinate zone, Vector2i position) {
		this(zone, position.x(), position.y());
	}

	public ChunkCoordinate up() {
		return new ChunkCoordinate(y() == 0 ? zone.up() : zone, x(), posMod(y() - 1, ZONE_SIZE));
	}

	public ChunkCoordinate down() {
		return new ChunkCoordinate(y() == ZONE_SIZE - 1 ? zone.down() : zone, x(), posMod(y() + 1, ZONE_SIZE));
	}

	public ChunkCoordinate left() {
		return new ChunkCoordinate(x() == 0 ? zone.left() : zone, posMod(x() - 1, ZONE_SIZE), y());
	}

	public ChunkCoordinate right() {
		return new ChunkCoordinate(x() == ZONE_SIZE - 1 ? zone.right() : zone, posMod(x() + 1, ZONE_SIZE), y());
	}

	/**
	 * Run this if you want to fix coordinates that are out of bound instead of doing the tough math to prevent it from
	 * happening in the first place. Calling this is probably a bug waiting to happen.
	 */
	public ChunkCoordinate normalize() {
		int x = posMod(x(), ZONE_SIZE);
		int y = posMod(y(), ZONE_SIZE);
		ZoneCoordinate zoneCoordinate = zone.normalize();
		if (x() < 0) {
			zoneCoordinate = zoneCoordinate.left();
		} else if (x() >= ZONE_SIZE) {
			zoneCoordinate = zoneCoordinate.right();
		}
		if (y() < 0) {
			zoneCoordinate = zoneCoordinate.up();
		} else if (y() >= ZONE_SIZE) {
			zoneCoordinate = zoneCoordinate.down();
		}
		return new ChunkCoordinate(zoneCoordinate, x, y);
	}

	/**
	 * Returns the chunk coordinate given a pixel position.
	 *
	 * @param position the position
	 * @return the chunk coordinate
	 */
	public static ChunkCoordinate chunkCoordinateOf(Vector2f position) {
		ZoneCoordinate zoneCoord = zoneCoordinateOf(position);
		Vector2f offset = new Vector2f()
				.add(new Vector2f(zoneCoord.region().x(), zoneCoord.region().y())).scale(REGION_SIZE)
				.add(new Vector2f(zoneCoord.x(), zoneCoord.y())).scale(ZONE_SIZE)
				.scale(CHUNK_SIZE)
				.scale(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING)
				.sub(position).negate();
		Vector2f tileToChunk = new Vector2f(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING)
				.scale(CHUNK_SIZE);
		return new ChunkCoordinate(zoneCoord,
				(int) floor(offset.x() / tileToChunk.x()),
				(int) floor(offset.y() / tileToChunk.y()))
				.normalize();
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

	public TileCoordinate[][] tileCoordinates() {
		TileCoordinate[][] tileCoords = new TileCoordinate[CHUNK_SIZE][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				tileCoords[x][y] = new TileCoordinate(this, x, y);
			}
		}
		return tileCoords;
	}

	@Override
	public String toString() {
		return zone.toString() + ".Chunk(" + x() + "," + y() + ")";
	}

	public ChunkCoordinateDiff sub(ChunkCoordinate chunk) {
		ZoneCoordinateDiff zoneDiff = zone.sub(chunk.zone());
		return new ChunkCoordinateDiff(zoneDiff, x() - chunk.x(), y() - chunk.y());
	}

}
