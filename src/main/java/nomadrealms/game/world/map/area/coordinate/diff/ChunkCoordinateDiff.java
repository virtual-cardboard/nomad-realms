package nomadrealms.game.world.map.area.coordinate.diff;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;

import common.math.Vector2f;

public class ChunkCoordinateDiff {

	private final ZoneCoordinateDiff zoneDiff;
	private final int x;
	private final int y;

	public ChunkCoordinateDiff(ZoneCoordinateDiff zoneDiff, int x, int y) {
		this.zoneDiff = zoneDiff;
		this.x = x;
		this.y = y;
	}

	public ZoneCoordinateDiff zoneDiff() {
		return zoneDiff;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public Vector2f toVector2f() {
		return new Vector2f(x, y)
				.scale(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING)
				.scale(CHUNK_SIZE)
				.add(zoneDiff.toVector2f());
	}

	@Override
	public String toString() {
		return "ChunkCoordinateDiff [x=" + x + ", y=" + y + "]";
	}

}
