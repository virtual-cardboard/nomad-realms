package nomadrealms.game;

import common.math.Vector2f;
import common.math.Vector2i;
import context.input.Mouse;
import nomadrealms.game.world.map.World;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.render.RenderingEnvironment;

import static nomadrealms.game.world.map.tile.Tile.screenToTile;

public class GameState {

	public int frameNumber = 0;
	public World world = new World();

	public void render(RenderingEnvironment re) {
		world.renderMap(re);
		world.renderActors(re);
	}

	public void update() {
		frameNumber++;
		world.update();
	}

	public Tile getMouseHexagon(Mouse mouse) {
		Vector2f cameraPosition = new Vector2f(0, 0);
		Vector2i coord = screenToTile(cameraPosition.add(mouse.coordinate().value().toVector()));
		return world.getTile(coord.x(), coord.y());
	}

	private Vector2f calculatePos(Vector2i cursor) {
		float chunkWidth = 100000; //inputInfo.settings.chunkWidth();
		float chunkHeight = 100000; //inputInfo.settings.chunkHeight();
		// The position in pixels from the top left corner of the current chunk
		float posX = (cursor.x() % chunkWidth + chunkWidth) % chunkWidth;
		float posY = (cursor.y() % chunkHeight + chunkHeight) % chunkHeight;
		return new Vector2f(posX, posY);
	}

}
