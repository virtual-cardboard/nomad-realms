package model.tile;

import common.math.Vector2i;

public class TileChunk {

	public static final int CHUNK_SIDE_LENGTH = 16;

	private Vector2i pos;
	private Tile[][] tiles;

	public TileChunk(Vector2i pos, Tile[][] tiles) {
		this.pos = pos;
		this.tiles = tiles;
		for (int row = 0; row < CHUNK_SIDE_LENGTH; row++) {
			for (int col = 0; col < CHUNK_SIDE_LENGTH; col++) {
				tiles[row][col].setChunk(this);
			}
		}
	}

	public TileChunk(Vector2i pos, TileType[][] tileTypes) {
		this.pos = pos;
		this.tiles = new Tile[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int row = 0; row < CHUNK_SIDE_LENGTH; row++) {
			for (int col = 0; col < CHUNK_SIDE_LENGTH; col++) {
				tiles[row][col] = new Tile(col, row, tileTypes[row][col]);
				tiles[row][col].setChunk(this);
			}
		}
	}

	public Vector2i pos() {
		return pos;
	}

	public Tile[][] tiles() {
		return tiles;
	}

	public Tile tile(int x, int y) {
		return tiles[y][x];
	}

}
