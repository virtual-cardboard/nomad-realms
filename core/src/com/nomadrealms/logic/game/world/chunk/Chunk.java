package com.nomadrealms.logic.game.world.chunk;

import com.nomadrealms.logic.game.world.tile.Tile;

public class Chunk {

	private static final int CHUNK_SIZE = 16;

	private Tile[][] tiles;

	public Chunk() {
		tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				tiles[x][y] = new Tile(x, y);
			}
		}
	}

	public Tile tile(int x, int y) {
		return tiles[x][y];
	}

}
