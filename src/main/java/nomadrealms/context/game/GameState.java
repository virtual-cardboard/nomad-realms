package nomadrealms.context.game;

import static nomadrealms.context.game.world.map.area.coordinate.TileCoordinate.tileCoordinateOf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import engine.common.math.Vector2f;
import engine.context.input.Mouse;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.MainWorldGenerationStrategy;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.Camera;

/**
 * The container for the {@link World} and all other data that represents the
 * current state of the game. This class is serialized to save and load the
 * game.
 * <p>
 * {@link InputEvent InputEvents} are the unit of synchronization between
 * clients, so {@link GameState} would be the snapshot of synchronization; they
 * must be synchronized within 30 frames of each other.
 *
 * @author Lunkle
 */
public class GameState {

	private String name = "Default World";

	public long frameNumber = 0;
	public World world;
	public boolean showMap = false;
	public Queue<InputEvent> uiEventChannel;
	final List<InputEventFrame> inputFrames = new ArrayList<>();

	/**
	 * No-arg constructor for serialization.
	 */
	protected GameState() {
		this("Default World", new LinkedList<>(), new MainWorldGenerationStrategy(123456789));
	}

	public GameState(String name, Queue<InputEvent> uiEventChannel, MapGenerationStrategy mapGenerationStrategy) {
		this.uiEventChannel = uiEventChannel;
		world = new World(this, mapGenerationStrategy);
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
		TileCoordinate coord = tileCoordinateOf(cameraPosition.add(mouse.coordinate().vector()));
		return world.getTile(coord);
	}

	public Tile getMouseHexagon(Mouse mouse) {
		Vector2f cameraPosition = new Vector2f(0, 0);
		TileCoordinate coord = tileCoordinateOf(cameraPosition.add(mouse.coordinate().vector()));
		return world.getTile(coord);
	}

	public String name() {
		return name;
	}

	public InputEventFrame lastInputFrame() {
		return inputFrames.get(inputFrames.size() - 1);
	}

	public void addEvent(InputEvent event) {
		lastInputFrame().addEvent(event);
	}

	/**
	 * Reinitialize any transient fields after loading from disk.
	 */
	public void reinitializeAfterLoad() {
		uiEventChannel = new LinkedList<>();
		world.reinitializeAfterLoad(this);
	}
}
