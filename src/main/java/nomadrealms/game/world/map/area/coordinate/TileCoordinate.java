package nomadrealms.game.world.map.area.coordinate;

import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;

public class TileCoordinate extends Coordinate {

	private ChunkCoordinate chunk;

	public TileCoordinate(ChunkCoordinate chunk, int x, int y) {
		super(x, y);
		this.chunk = chunk;
	}

	@Override
	public TileCoordinate up() {
		return new TileCoordinate(y() == CHUNK_SIZE - 1 ? chunk.up() : chunk, x(), y() + 1);
	}

	@Override
	public TileCoordinate down() {
		return new TileCoordinate(y() == 0 ? chunk.down() : chunk, x(), y() - 1);
	}

	@Override
	public TileCoordinate left() {
		return new TileCoordinate(x() == 0 ? chunk.left() : chunk, x() - 1, y());
	}

	@Override
	public TileCoordinate right() {
		return new TileCoordinate(x() == CHUNK_SIZE - 1 ? chunk.right() : chunk, x() + 1, y());
	}

	@Override
	public RegionCoordinate region() {
		return chunk.region();
	}

	public ChunkCoordinate chunk() {
		return chunk;
	}

}
