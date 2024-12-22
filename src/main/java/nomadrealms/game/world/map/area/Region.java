package nomadrealms.game.world.map.area;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.zoneCoordinateOf;

import common.math.Vector2f;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;

/**
 * A region is a 3x3 grid of zones. This is the optimal size for getting good network synchronization, you will see this
 * a lot in the net-code.
 * <br><br>
 * Some math:
 * <br>
 * Region Dimensions = ({@link Tile#TILE_HORIZONTAL_SPACING TILE_X}, {@link Tile#TILE_VERTICAL_SPACING TILE_Y}) *
 * {@link ChunkCoordinate#CHUNK_SIZE CHUNK SIZE} * {@link ZoneCoordinate#ZONE_SIZE ZONE_SIZE} *
 * {@link RegionCoordinate#REGION_SIZE REGION_SIZE} = (30, 34.64) * 16 * 16 * 3 = (23040, 26603.52)
 */
public class Region {

	private long timestamp;
	private final transient MapGenerationStrategy strategy;
	private final transient World world;

	private final RegionCoordinate coord;

	private final Zone[][] zones;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Region() {
		this(null, null, null);
	}

	public Region(MapGenerationStrategy strategy, World world, RegionCoordinate coord) {
		this.strategy = strategy;
		this.world = world;
		this.coord = coord;
		zones = new Zone[REGION_SIZE][REGION_SIZE];
	}

	public void render(RenderingEnvironment re, Vector2f origin) {
		ZoneCoordinate zoneCoord = zoneCoordinateOf(origin);
		lazyGetZone(zoneCoord).render(re, origin);
	}

	private Vector2f indexPosition() {
		return new Vector2f(coord.x() * TILE_HORIZONTAL_SPACING, coord.y() * TILE_VERTICAL_SPACING).scale(REGION_SIZE * ZONE_SIZE * CHUNK_SIZE);
	}

	public Vector2f pos() {
		return indexPosition();
	}

	public RegionCoordinate coord() {
		return coord;
	}

	private Zone lazyGetZone(ZoneCoordinate zoneCoord) {
		assert zoneCoord.region().equals(coord);
		int x = zoneCoord.x();
		int y = zoneCoord.y();
		if (zones[x][y] == null) {
			zones[x][y] = new Zone(world, zoneCoord, strategy);
		}
		return zones[x][y];
	}

	public Tile getTile(TileCoordinate tile) {
		assert tile.region().equals(coord);
		return lazyGetZone(tile.zone()).getTile(tile);
	}

	public World world() {
		return world;
	}

	public Chunk getChunk(ChunkCoordinate chunkCoord) {
		return lazyGetZone(chunkCoord.zone()).getChunk(chunkCoord);
	}

}
