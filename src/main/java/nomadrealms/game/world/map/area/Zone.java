package nomadrealms.game.world.map.area;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import common.math.Vector2f;
import common.math.Vector2i;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;

/**
 * A zone is a 16x16 grid of chunks. This is the optimal size for getting good layer-based map generation results.
 */
public class Zone {

	private final Region region;
	private final ZoneCoordinate coord;

	private final Chunk[][] chunks;

	public Zone(World world, ZoneCoordinate coord, MapGenerationStrategy strategy) {
		this.region = world.getRegion(coord.region());
		this.coord = coord;
		this.chunks = strategy.generateZone(world, this);
	}

	public void render(RenderingEnvironment re) {
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunks[x][y].render(re);
			}
		}
	}

	private Vector2i chunkIndexOf(Vector2f position) {
		Vector2f offset = new Vector2f(
				position.x() - ZONE_SIZE * CHUNK_SIZE * coord.x(),
				position.y() - ZONE_SIZE * CHUNK_SIZE * coord.y());
		return new Vector2i((int) (offset.x() / CHUNK_SIZE), (int) (offset.y() / CHUNK_SIZE));
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
