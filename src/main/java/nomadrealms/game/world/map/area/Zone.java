package nomadrealms.game.world.map.area;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import common.math.Vector2f;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.render.RenderingEnvironment;

/**
 * A zone is a 16x16 grid of chunks. This is the optimal size for getting good layer-based map generation results.
 */
public class Zone {

	private final Region region;
	private final ZoneCoordinate coord;

	private final Chunk[][] chunks;

	public Zone(Region region, ZoneCoordinate coord) {
		this.region = region;
		this.coord = coord;
		this.chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
	}

	public Zone(World world, ZoneCoordinate coord) {
		this(world.getRegion(coord), coord);
	}

	public void render(RenderingEnvironment re) {
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunks[x][y].render(re);
			}
		}
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

}
