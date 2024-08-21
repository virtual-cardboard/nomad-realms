package nomadrealms.game.world.map.area.coordinate;

import static common.math.MathUtil.posMod;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;

public class TileCoordinate extends Coordinate {

	private final ChunkCoordinate chunk;

	public TileCoordinate(ChunkCoordinate chunk, int x, int y) {
		super(x, y);
		this.chunk = chunk;
	}

	// Up Left
	public TileCoordinate ul() {
		int tileY = posMod(x() % 2 == 0? y() - 1 : y(), 16);
		int tileX = posMod(x() - 1, 16);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == 0 ? chunkCoord.up() : chunkCoord;
		chunkCoord = x() == 0 ? chunkCoord.left() : chunkCoord;

		return new TileCoordinate(chunkCoord, tileX, tileY);
	}

	// Up Middle
	public TileCoordinate um() {
		int tileY = posMod(y() - 1, 16);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == 0 ? chunkCoord.up() : chunkCoord;

		return new TileCoordinate(chunkCoord, x(), tileY);
	}

	// Up Right
	public TileCoordinate ur() {
		int tileY = posMod(x() % 2 == 0? y() - 1 : y(), 16);
		int tileX = posMod(x() + 1, 16);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == 0 ? chunkCoord.up() : chunkCoord;
		chunkCoord = x() == CHUNK_SIZE - 1 ? chunkCoord.right() : chunkCoord;

		return new TileCoordinate(chunkCoord, tileX, tileY);
	}

	// Down Left
	public TileCoordinate dl() {
		int tileY = posMod(x() % 2 == 0? y() : y() + 1, 16);
		int tileX = posMod(x() - 1, 16);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == CHUNK_SIZE - 1 ? chunkCoord.down() : chunkCoord;
		chunkCoord = x() == 0 ? chunkCoord.left() : chunkCoord;

		return new TileCoordinate(chunkCoord, tileX, tileY);
	}

	// Down Middle
	public TileCoordinate dm() {
		int tileY = posMod(y() + 1, 16);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == CHUNK_SIZE - 1 ? chunkCoord.down() : chunkCoord;

		return new TileCoordinate(chunkCoord, x(), tileY);
	}

	// Down Right
	public TileCoordinate dr() {
		int tileY = posMod(x() % 2 == 0? y() : y() + 1, 16);
		int tileX = posMod(x() + 1, 16);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == CHUNK_SIZE - 1 ? chunkCoord.down() : chunkCoord;
		chunkCoord = x() == CHUNK_SIZE - 1 ? chunkCoord.right() : chunkCoord;

		return new TileCoordinate(chunkCoord, tileX, tileY);
	}

	@Override
	public RegionCoordinate region() {
		return chunk.region();
	}

	public ZoneCoordinate zone() {
		return chunk.zone();
	}

	public ChunkCoordinate chunk() {
		return chunk;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TileCoordinate) {
			TileCoordinate other = (TileCoordinate) o;
			return x() == other.x() && y() == other.y() && chunk.equals(other.chunk);
		}
		return false;
	}

}
