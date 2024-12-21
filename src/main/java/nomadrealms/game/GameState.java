package nomadrealms.game;

import static nomadrealms.game.world.map.area.coordinate.TileCoordinate.tileCoordinateOf;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import common.math.Vector2f;
import context.input.Mouse;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.InputEventFrame;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.Camera;

/**
 * The game state is the container for the {@link World}, along with additional metadata about the world and input
 * events.
 * <p></p>
 * {@link InputEvent InputEvents} are the unit of synchronization between clients, so {@link GameState} would be the
 * snapshot of synchronization; they must be synchronized within 30 frames of each other.
 */
public class GameState {

	public long frameNumber = 0;
	public World world = new World(this);
	public boolean showMap = false;
	public Queue<InputEvent> uiEventChannel;
	final List<InputEventFrame> inputFrames = new ArrayList<>();

	public GameState(Queue<InputEvent> uiEventChannel) {
		this.uiEventChannel = uiEventChannel;
	}

	public void render(RenderingEnvironment re) {
		re.camera.update();
		world.renderMap(re);
		world.renderActors(re);
	}

	public void update() {
		frameNumber++;
		inputFrames.add(new InputEventFrame(frameNumber));
		if (inputFrames.size() > 30) {
			inputFrames.remove(0);
		}
		world.update(lastInputFrame());
	}

	public Tile getMouseHexagon(Mouse mouse, Camera camera) {
		Vector2f cameraPosition = camera.position();
		TileCoordinate coord = tileCoordinateOf(cameraPosition.add(mouse.coordinate().value().toVector()));
		return world.getTile(coord);
	}

	public Tile getMouseHexagon(Mouse mouse) {
		Vector2f cameraPosition = new Vector2f(0, 0);
		TileCoordinate coord = tileCoordinateOf(cameraPosition.add(mouse.coordinate().value().toVector()));
		return world.getTile(coord);
	}

	public InputEventFrame lastInputFrame() {
		return inputFrames.get(inputFrames.size() - 1);
	}

	public void addEvent(InputEvent event) {
		lastInputFrame().addEvent(event);
	}

	public void saveToFile(String filePath) {
		try {
			GameStateSerializer.serialize(this, filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadFromFile(String filePath) {
		try {
			GameState loadedState = GameStateSerializer.deserialize(filePath);
			this.frameNumber = loadedState.frameNumber;
			this.world = loadedState.world;
			this.showMap = loadedState.showMap;
			this.uiEventChannel = loadedState.uiEventChannel;
			this.inputFrames.clear();
			this.inputFrames.addAll(loadedState.inputFrames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
