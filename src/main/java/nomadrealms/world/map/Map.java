package nomadrealms.world.map;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.world.map.tile.Tile;

public class Map {

	private Chunk chunk;

	public Map() {
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
