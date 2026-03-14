package nomadrealms.app.context;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import engine.common.math.Matrix4f;
import engine.common.time.FPSCounter;
import engine.context.GameContext;
import engine.context.input.event.CharacterTypedInputEvent;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import java.util.LinkedList;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.context.game.world.map.generation.TerrainSandboxMapInitialization;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.Camera;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.render.ui.content.TextContent;
import nomadrealms.render.ui.custom.Ruler;
import nomadrealms.render.ui.custom.console.Console;

public class TerrainSandboxContext extends GameContext {

	private RenderingEnvironment re;
	private GameState gameState;
	private Console console;
	private final Ruler ruler = new Ruler();
	private final FPSCounter fpsCounter = new FPSCounter(100);
	private TextContent fpsText;

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	private ScreenContainerContent ui;
	private ButtonUIContent pausePlayButton;
	private boolean paused = false;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		re.camera = new Camera(0, 0);
		re.camera.position(glContext().screen.dimensions().scale(-0.5f).vector());

		initGameState(123456789);

		ui = new ScreenContainerContent(re);
		pausePlayButton = new ButtonUIContent(ui, "Pause",
				new ConstraintBox(absolute(20), absolute(60), absolute(100), absolute(40)),
				this::togglePaused);
		pausePlayButton.registerCallbacks(inputCallbackRegistry);

		fpsText = new TextContent(() -> String.format("FPS: %.1f", fpsCounter.getFPS()), 1000, 20, re.font,
				new ConstraintPair(absolute(20), absolute(20)), 0);
	}

	private void initGameState(long seed) {
		gameState = new GameState("Terrain Sandbox", new LinkedList<>(),
				new OverworldGenerationStrategy(seed).mapInitialization(new TerrainSandboxMapInitialization()));
		console = new Console(glContext().screen, gameState, re);
		console.customCommandProcessor((cmd, args) -> {
			if (cmd.equalsIgnoreCase("REGEN")) {
				try {
					long newSeed = args.length > 1 ? Long.parseLong(args[1]) : System.currentTimeMillis();
					initGameState(newSeed);
					return "Regenerated world with seed: " + newSeed;
				} catch (NumberFormatException e) {
					return "Invalid seed: " + args[1];
				}
			}
			return null;
		});
	}

	private void togglePaused() {
		paused = !paused;
		ui.clearChildren();
		inputCallbackRegistry.clear();
		pausePlayButton = new ButtonUIContent(ui, paused ? "Play" : "Pause",
				pausePlayButton.constraintBox(), this::togglePaused);
		pausePlayButton.registerCallbacks(inputCallbackRegistry);
	}

	@Override
	public void update() {
		re.camera.update();
		if (!paused && gameState != null) {
			gameState.update();
		}
	}

	@Override
	public void render(float alpha) {
		fpsCounter.update();
		re.fbo1.render(() -> {
			background(0);
			gameState.render(re);
		});

		DefaultFrameBuffer.instance().render(() -> {
			background(gameState.weather.skyColor(gameState.frameNumber));
			re.textureRenderer.render(re.fbo1.texture(), new Matrix4f(glContext().screen, glContext()));
			console.render(re);
			ui.render(re);
			ruler.render(re);
			if (re.showDebugInfo) {
				fpsText.render(re);
			}
		});
	}

	@Override
	public void cleanUp() {
	}

	@Override
	public void input(KeyPressedInputEvent event) {
		int key = event.code();
		if (console.active()) {
			console.handleKey(key);
			return;
		}
		if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER) {
			console.active(true);
			return;
		}
		if (key == GLFW_KEY_SPACE) {
			togglePaused();
			return;
		}
		switch (key) {
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
				break;
			default:
				break;
		}
	}

	@Override
	public void input(CharacterTypedInputEvent event) {
		if (console.active()) {
			console.handleChar(event.codepoint());
		}
	}

	@Override
	public void input(KeyReleasedInputEvent event) {
		int key = event.code();
		if (console.active()) {
			return;
		}
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
				break;
			default:
				break;
		}
	}

	@Override
	public void input(MouseScrolledInputEvent event) {
		float amount = event.yAmount();
		re.camera.zoom(re.camera.zoom().get() * (float) Math.pow(1.1f, amount), event.mouse());
	}

	@Override
	public void input(MouseMovedInputEvent event) {
		inputCallbackRegistry.triggerOnDrag(event);
	}

	@Override
	public void input(MousePressedInputEvent event) {
		inputCallbackRegistry.triggerOnPress(event);
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		inputCallbackRegistry.triggerOnDrop(event);
	}

}
