package nomadrealms.world.map;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.world.map.tile.Tile;

public class World {

	private Chunk chunk;

	public World() {
		chunk = new Chunk();
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void render(RenderingEnvironment renderingEnvironment) {
		chunk.render(renderingEnvironment);
	}

	public Tile getTile(int row, int col) {
		return chunk.getTile(row, col);
	}

}
