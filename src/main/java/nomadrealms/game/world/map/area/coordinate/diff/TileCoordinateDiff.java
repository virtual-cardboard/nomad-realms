package nomadrealms.game.world.map.area.coordinate.diff;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;

import engine.common.math.Vector2f;

public class TileCoordinateDiff {

	private final ChunkCoordinateDiff chunkDiff;
	private final int x;
	private final int y;
	private final int offset;

	public TileCoordinateDiff(ChunkCoordinateDiff chunkDiff, int x, int y, int offset) {
		this.chunkDiff = chunkDiff;
		this.x = x;
		this.y = y;
		this.offset = offset;
	}

	public ChunkCoordinateDiff zoneDiff() {
		return chunkDiff;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public Vector2f toVector2f() {
		return new Vector2f(x, y)
				.add(0, offset * 0.5f)
				.scale(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING)
				.add(chunkDiff.toVector2f());
	}

	@Override
	public String toString() {
		return "TileCoordinateDiff [x=" + x + ", y=" + y + "]";
	}

}
