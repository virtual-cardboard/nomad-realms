package model.map;

import model.map.tile.Tile;
import model.map.tile.TileType;

public class TileMap {

	private Tile[][] tiles;

	public TileMap(TileType[][] tileTypes) {
		tiles = new Tile[tileTypes.length][tileTypes[0].length];
		for (int i = 0; i < tileTypes.length; i++) {
			for (int j = 0; j < tileTypes[i].length; j++) {
				tiles[i][j] = new Tile(j, i, tileTypes[i][j]);
			}
		}
	}

	public Tile tile(int x, int y) {
		return tiles[y][x];
	}

	public int width() {
		return tiles[0].length;
	}

	public int height() {
		return tiles.length;
	}

}
