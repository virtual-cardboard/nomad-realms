package com.nomadrealms.math.coordinate.map;

import static com.nomadrealms.math.coordinate.map.ChunkCoordinate.CHUNK_SIZE;

import com.nomadrealms.math.coordinate.Coordinate;

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

	public ChunkCoordinate chunk() {
		return chunk;
	}

}
