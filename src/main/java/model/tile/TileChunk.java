package model.tile;

import static model.tile.Tile.TILE_HEIGHT;
import static model.tile.Tile.TILE_WIDTH;

import common.math.Vector2i;

public class TileChunk {

	public static final int CHUNK_SIDE_LENGTH = 16;
	public static final int CHUNK_WIDTH = TILE_WIDTH * CHUNK_SIDE_LENGTH * 3 / 4;
	public static final int CHUNK_HEIGHT = TILE_HEIGHT * CHUNK_SIDE_LENGTH;

	private Vector2i pos;
	private Tile[][] tiles;

	public TileChunk(Vector2i pos, TileType[][] tileTypes) {
		this.pos = pos;
		this.tiles = new Tile[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int row = 0; row < CHUNK_SIDE_LENGTH; row++) {
			for (int col = 0; col < CHUNK_SIDE_LENGTH; col++) {
				tiles[row][col] = new Tile(col, row, tileTypes[row][col], this);
			}
		}
	}

	public static Vector2i chunkPos(long tileID) {
		int cx = (int) (tileID << 8 >> 36);
		int cy = (int) (tileID << 36 >> 36);
		return new Vector2i(cx, cy);
	}

	public Vector2i pos() {
		return pos;
	}

	public Tile[][] tiles() {
		return tiles;
	}

	public Tile tile(Vector2i pos) {
		return tiles[pos.y][pos.x];
	}

	public Tile tile(int x, int y) {
		return tiles[y][x];
	}

}
