package nomadrealms.world.map;

import nomadrealms.render.RenderingEnvironment;

public class Chunk {

	private Tile[][] tiles;

	public Chunk() {
		tiles = new Tile[16][16];
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				tiles[x][y] = new Tile(x, y);
			}
		}
	}

	public void render(RenderingEnvironment renderingEnvironment) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				tiles[x][y].render(renderingEnvironment);
			}
		}
	}

}
