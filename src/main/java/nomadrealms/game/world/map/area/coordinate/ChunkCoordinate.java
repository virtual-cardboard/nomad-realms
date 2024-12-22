package nomadrealms.game.world.map.area.coordinate;

import static common.math.MathUtil.posMod;
import static java.lang.Math.floor;
import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.zoneCoordinateOf;

import common.math.Vector2f;
import common.math.Vector2i;

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
				(int) floor(offset.y() / tileToChunk.y()));
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

	@Override
	public String toString() {
		return "ChunkCoordinate(" + x() + ", " + y() + ")";
	}

}
