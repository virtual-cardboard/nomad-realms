package com.nomadrealms.logic.game.world.area;

import static com.nomadrealms.rendering.basic.shape.HexagonShapeRenderer.renderHexagon;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.logic.game.world.tile.Tile;

public class Chunk {

	private static final int CHUNK_SIZE = 16;

	private Vector2 pos;

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

	public void render(Vector2 camera) {
		renderHexagon(1, 1, 100);
	}

}
