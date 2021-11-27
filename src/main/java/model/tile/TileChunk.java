package model.tile;

import static model.tile.Tile.TILE_HEIGHT;
import static model.tile.Tile.TILE_WIDTH;

import common.math.Vector2i;

public class TileChunk {

	public static final int CHUNK_SIDE_LENGTH = 16;
	public static final int CHUNK_PIXEL_WIDTH = TILE_WIDTH * CHUNK_SIDE_LENGTH * 3 / 4 + TILE_WIDTH / 4;
	public static final int CHUNK_PIXEL_HEIGHT = TILE_HEIGHT * CHUNK_SIDE_LENGTH * 3 / 4 + TILE_HEIGHT / 4;

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

	public static Vector2i chunkPos(long tileID) {
		int cx = (int) ((tileID >>> 28) & 0xFFFFFFF);
		int cy = (int) (tileID & 0xFFFFFFF);
		return new Vector2i(cx, cy);
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
