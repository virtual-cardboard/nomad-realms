package com.nomadrealms.logic.game.world.area;

import static com.nomadrealms.logic.game.world.area.Tile.TILE_HORIZONTAL_SPACING;
import static com.nomadrealms.logic.game.world.area.Tile.TILE_VERTICAL_SPACING;
import static com.nomadrealms.math.coordinate.map.ChunkCoordinate.CHUNK_SIZE;
import static com.nomadrealms.math.coordinate.map.ZoneCoordinate.ZONE_SIZE;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.logic.game.world.World;
import com.nomadrealms.math.coordinate.map.ZoneCoordinate;

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

	public void render(Vector2 camera) {
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunks[x][y].render(camera);
			}
		}
	}

	public void setChunk(int x, int y, Chunk chunk) {
		chunks[x][y] = chunk;
	}

	private Vector2 indexPosition() {
		return new Vector2(coord.x(), coord.y()).scl(ZONE_SIZE * CHUNK_SIZE).scl(TILE_HORIZONTAL_SPACING,
				TILE_VERTICAL_SPACING);
	}

	public Vector2 pos() {
		return region.pos().add(indexPosition());
	}

}
