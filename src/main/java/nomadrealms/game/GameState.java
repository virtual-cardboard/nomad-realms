package nomadrealms.game;

import common.math.Vector2f;
import common.math.Vector2i;
import context.input.Mouse;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.InputEventFrame;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.render.RenderingEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static nomadrealms.game.world.map.tile.Tile.screenToTile;

public class GameState {

	public long frameNumber = 0;
	public World world = new World(this);
	public Queue<InputEvent> uiEventChannel;
	private List<InputEventFrame> inputFrames = new ArrayList<>();

	public GameState(Queue<InputEvent> uiEventChannel) {
		this.uiEventChannel = uiEventChannel;
	}

	public void render(RenderingEnvironment re) {
		world.renderMap(re);
		world.renderActors(re);
		re.camera.update();
	}

	public void update() {
		frameNumber++;
		inputFrames.add(new InputEventFrame(frameNumber));
		if (inputFrames.size() > 30) {
			inputFrames.remove(0);
		}
		world.update(lastInputFrame());
	}

	public Tile getMouseHexagon(Mouse mouse) {
		Vector2f cameraPosition = new Vector2f(0, 0);
		Vector2i coord = screenToTile(cameraPosition.add(mouse.coordinate().value().toVector()));
		// TODO first calculate which chunk the mouse is on
		return world.getTile(new Vector2i(0, 0), coord.x(), coord.y());
	}

	private Vector2f calculatePos(Vector2i cursor) {
		float chunkWidth = 100000; //inputInfo.settings.chunkWidth();
		float chunkHeight = 100000; //inputInfo.settings.chunkHeight();
		// The position in pixels from the top left corner of the current chunk
		float posX = (cursor.x() % chunkWidth + chunkWidth) % chunkWidth;
		float posY = (cursor.y() % chunkHeight + chunkHeight) % chunkHeight;
		return new Vector2f(posX, posY);
	}

	public InputEventFrame lastInputFrame() {
		return inputFrames.get(inputFrames.size() - 1);
	}

	public void addEvent(InputEvent event) {
		lastInputFrame().addEvent(event);
	}

}