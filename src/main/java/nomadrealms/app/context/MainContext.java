package nomadrealms.app.context;

import static common.colour.Colour.rgb;
import static nomadrealms.game.world.map.area.Tile.showTileCoordinates;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.HUMIDITY_CEIL;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.HUMIDITY_FLOOR;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.TEMPERATURE_CEIL;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.TEMPERATURE_FLOOR;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import context.GameContext;
import context.input.event.KeyPressedInputEvent;
import context.input.event.KeyReleasedInputEvent;
import context.input.event.MouseMovedInputEvent;
import context.input.event.MousePressedInputEvent;
import context.input.event.MouseReleasedInputEvent;
import context.input.event.MouseScrolledInputEvent;
import nomadrealms.game.GameState;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.generation.status.biome.BiomeParameters;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.GameInterface;

/**
 * The main context of the game. Everything important can be found originating through here.
 * <p></p>
 * As a game context, this class is responsible for updating, rendering, and handling input.
 * <p></p>
 * Notable variables:
 * <ul>
 *     <li>{@link GameInterface ui} - everything rendered on top of the game that the user interacts with</li>
 *     <li>{@link GameState gameState} - everything related to the actual game</li>
 * </ul>
 */
public class MainContext extends GameContext {

	RenderingEnvironment re;
	GameInterface ui;

	private final Queue<InputEvent> stateToUiEventChannel = new ArrayDeque<>();

	GameState gameState = new GameState(stateToUiEventChannel);

	List<Consumer<MousePressedInputEvent>> onClick = new ArrayList<>();
	List<Consumer<MouseMovedInputEvent>> onDrag = new ArrayList<>();
	List<Consumer<MouseReleasedInputEvent>> onDrop = new ArrayList<>();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext());
		ui = new GameInterface(stateToUiEventChannel, gameState, glContext(), mouse(), onClick, onDrag, onDrop);
	}

	@Override
	public void update() {
		gameState.update();
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		gameState.render(re);
		ui.render(re);
	}

	@Override
	public void terminate() {
		System.out.println("second context terminate");
	}

	public void input(KeyPressedInputEvent event) {
		int key = event.code();
		switch (key) {
			case GLFW_KEY_E:
				gameState.world.nomad.inventory().toggle();
				break;
			case GLFW_KEY_M:
				gameState.showMap = !gameState.showMap;
				break;
			case GLFW_KEY_W:
				re.camera.up(true);
				break;
			case GLFW_KEY_A:
				re.camera.left(true);
				break;
			case GLFW_KEY_S:
				re.camera.down(true);
				break;
			case GLFW_KEY_D:
				re.camera.right(true);
				break;
			case GLFW_KEY_F3:
				showTileCoordinates = true;
			default:
				System.out.println("second context key pressed: " + key);
		}
	}

	public void input(KeyReleasedInputEvent event) {
		int key = event.code();
		switch (key) {
			case GLFW_KEY_W:
				re.camera.up(false);
				break;
			case GLFW_KEY_A:
				re.camera.left(false);
				break;
			case GLFW_KEY_S:
				re.camera.down(false);
				break;
			case GLFW_KEY_D:
				re.camera.right(false);
				break;
			case GLFW_KEY_F3:
				showTileCoordinates = false;
			default:
				System.out.println("second context key released: " + key);
		}
	}

	public void input(MouseScrolledInputEvent event) {
		float amount = event.yAmount();
		System.out.println("second context mouse scrolled: " + amount);
	}

	@Override
	public void input(MouseMovedInputEvent event) {
		for (Consumer<MouseMovedInputEvent> r : onDrag) {
			r.accept(event);
		}
	}

	@Override
	public void input(MousePressedInputEvent event) {
		Tile tile = gameState.getMouseHexagon(mouse(), re.camera);
		if (tile != null) {
			Zone zone = tile.zone();
			System.out.println();
			System.out.println("================================");
			System.out.println(tile.coord());
			BiomeParameters p = zone.biomeGenerationStep().parametersAt(tile.coord());
			System.out.println(p);
			float adjustedTemperature =
					(p.temperature() + 1) * (TEMPERATURE_CEIL - TEMPERATURE_FLOOR) / 2 + TEMPERATURE_FLOOR;
			float adjustedHumidity = (p.humidity() + 1) * (HUMIDITY_CEIL - HUMIDITY_FLOOR) / 2 + HUMIDITY_FLOOR;
			System.out.println("adjusted temperature: " + adjustedTemperature);
			System.out.println("adjusted humidity: " + adjustedHumidity);
			System.out.println(zone.biomeGenerationStep().continentAt(tile.coord()));
			System.out.println(zone.biomeGenerationStep().categoryAt(tile.coord()));
			System.out.println(zone.biomeGenerationStep().biomeAt(tile.coord()));
		}
		for (Consumer<MousePressedInputEvent> r : onClick) {
			r.accept(event);
		}
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		for (Consumer<MouseReleasedInputEvent> r : onDrop) {
			r.accept(event);
		}
	}

}
