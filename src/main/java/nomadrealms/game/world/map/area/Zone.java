package nomadrealms.game.world.map.area;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.chunkCoordinateOf;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import common.math.Vector2f;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;

/**
 * A zone is a 16x16 grid of chunks. This is the optimal size for getting good layer-based map generation results, you
 * will see this a lot in the map generation code.
 * <br><br>
 * Some math:
 * <br>
 * Zone Dimensions = ({@link Tile#TILE_HORIZONTAL_SPACING TILE_X}, {@link Tile#TILE_VERTICAL_SPACING TILE_Y}) *
 * {@link ChunkCoordinate#CHUNK_SIZE CHUNK SIZE} = (30, 34.64) * 16 * 16 = (7680, 8867.84)
 * <br>
 */
public class Zone {

	private final transient Region region;
	private final ZoneCoordinate coord;

	private final Chunk[][] chunks;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Zone() {
		this.region = null;
		this.coord = null;
		this.chunks = null;
	}

	public Zone(World world, ZoneCoordinate coord, MapGenerationStrategy strategy) {
		this.region = world.getRegion(coord.region());
		this.coord = coord;
		this.chunks = strategy.generateZone(world, this);
	}

	public void render(RenderingEnvironment re, Vector2f origin) {
		ChunkCoordinate chunkCoord = chunkCoordinateOf(origin);
		getChunk(chunkCoord).render(re);
		region.world().getChunk(chunkCoord.up()).render(re);
		region.world().getChunk(chunkCoord.down()).render(re);
		region.world().getChunk(chunkCoord.left()).render(re);
		region.world().getChunk(chunkCoord.right()).render(re);
	}

	Chunk getChunk(ChunkCoordinate chunkCoord) {
		assert chunkCoord.zone().equals(coord);
		return chunks[chunkCoord.x()][chunkCoord.y()];
	}

	public void setChunk(int x, int y, Chunk chunk) {
		chunks[x][y] = chunk;
	}

	private Vector2f indexPosition() {
		return new Vector2f(
				coord.x() * TILE_HORIZONTAL_SPACING,
				coord.y() * TILE_VERTICAL_SPACING)
				.scale(ZONE_SIZE * CHUNK_SIZE);
	}

	public Vector2f pos() {
		return region.pos().add(indexPosition());
	}

	public Tile getTile(TileCoordinate tile) {
		assert tile.zone().equals(coord);
		return chunks[tile.chunk().x()][tile.chunk().y()].getTile(tile);
	}

	public ZoneCoordinate coord() {
		return coord;
	}

}
