package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.world.map.generation.MainWorldGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.game.GameInterface;
import nomadrealms.user.data.GameData;

/**
 * The main context of the game. Everything important can be found originating through here.
 * <p>
 * </p>
 * As a game context, this class is responsible for updating, rendering, and handling input.
 * <p>
 * </p>
 * Notable variables:
 * <ul>
 * <li>{@link GameInterface ui} - everything rendered on top of the game that
 * the user interacts with</li>
 * <li>{@link GameState gameState} - everything related to the actual game</li>
 * </ul>
 */
public class MainContext extends GameContext {

	RenderingEnvironment re;
	GameInterface ui;
	private final Queue<InputEvent> stateToUiEventChannel = new ArrayDeque<>();

	private GameData data;

	private GameState gameState;

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	public MainContext(GameData data) {
		this.data = data;
	}

	@Override
	public void init() {
		List<Supplier<GameState>> gameStates = data.saves().fetch();
		if (gameStates.isEmpty()) {
			gameState = new GameState("New World", stateToUiEventChannel, new MainWorldGenerationStrategy(123456789));
		} else {
			gameState = gameStates.get(0).get();
			gameState.reinitializeAfterLoad(stateToUiEventChannel);
		}
		re = new RenderingEnvironment(glContext(), config());
		ui = new GameInterface(re, stateToUiEventChannel, gameState, glContext(), mouse(), inputCallbackRegistry);
	}

	@Override
	public void update() {
		if (gameState != null) {
			gameState.update();
		}
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		gameState.render(re);
		ui.render(re);
	}

	@Override
	public void cleanUp() {
		System.out.println("Saving game");
		data.saves().writeGameState(gameState);
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
				re.showDebugInfo = true;
			default:
				break;
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
				re.showDebugInfo = false;
			default:
				break;
		}
	}

	public void input(MouseScrolledInputEvent event) {
		float amount = event.yAmount();
	}

	@Override
	public void input(MouseMovedInputEvent event) {
		inputCallbackRegistry.triggerOnDrag(event);
	}

	@Override
	public void input(MousePressedInputEvent event) {
		switch (event.button()) {
			case GLFW_MOUSE_BUTTON_LEFT:
			default:
				break;
		}
		inputCallbackRegistry.triggerOnPress(event);
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		inputCallbackRegistry.triggerOnDrop(event);
	}

}
