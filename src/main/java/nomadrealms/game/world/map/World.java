package nomadrealms.game.world.map;

import nomadrealms.game.world.actor.Nomad;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.game.world.map.tile.Tile;

public class World {

	private Chunk chunk;
	public Nomad nomad;

	public World() {
		chunk = new Chunk();
		nomad = new Nomad("Donny", getTile(1, 0));
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void render(RenderingEnvironment re) {
		chunk.render(re);
		nomad.render(re);
	}

	int x = 0;
	int i = 0;
	public void update() {
		i++;
		if (i % 10 == 0) {
			x = Math.min(x + 1, 15);
			nomad.tile(getTile(x, 2));
			i = 0;
		}
	}

	public Tile getTile(int row, int col) {
		return chunk.getTile(row, col);
	}

}
