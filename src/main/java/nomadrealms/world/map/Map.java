package nomadrealms.world.map;

import nomadrealms.render.RenderingEnvironment;

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

}
