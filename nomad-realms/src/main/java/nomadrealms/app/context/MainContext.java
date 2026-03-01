package nomadrealms.app.context;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

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
import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkingSender;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import nomadrealms.audio.MusicPlayer;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.generation.DefaultMapInitialization;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.ui.Camera;
import nomadrealms.render.ui.content.TextContent;
import nomadrealms.render.ui.custom.Ruler;
import nomadrealms.render.ui.custom.console.Console;
import nomadrealms.render.ui.custom.game.GameInterface;
import nomadrealms.render.ui.custom.indicator.PlayerIndicator;
import nomadrealms.user.Player;
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

	private final GameData data = new GameData();

	private RenderingEnvironment re;
	private GameInterface ui;
	private PlayerIndicator playerIndicator = new PlayerIndicator();
	private Console console;
	private final Ruler ruler = new Ruler();
	private final FPSCounter fpsCounter = new FPSCounter(100);
	private TextContent fpsText;
	private final Queue<InputEvent> stateToUiEventChannel = new ArrayDeque<>();

	private final NetworkingSender networkingSender = new NetworkingSender();

	private final GameState gameState;

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	private MusicPlayer musicPlayer;

	private Player localPlayer;
	private final List<Player> onlinePlayers = new ArrayList<>();

	public MainContext() {
		this(new Deck(), new Deck(), new Deck(), new Deck());
	}

	public MainContext(Deck deck1, Deck deck2, Deck deck3, Deck deck4) {
		gameState = new GameState("New World", stateToUiEventChannel, new OverworldGenerationStrategy(123456789)
				.mapInitialization(new DefaultMapInitialization()));
		gameState.world.nomad.deckCollection().importDecks(deck1, deck2, deck3, deck4);
	}

	public MainContext(GameState gameState) {
		this.gameState = gameState;
		gameState.reindex(stateToUiEventChannel);
	}

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		localPlayer = new Player("Local Player", new PacketAddress()).cardPlayer(gameState.world.nomad);
		re.camera = new Camera(localPlayer.cardPlayer().tile().pos().sub(glContext().screen.dimensions().scale(0.5f * 0.6f, 0.5f)));
		ui = new GameInterface(re, localPlayer, stateToUiEventChannel, gameState, glContext(), mouse(), inputCallbackRegistry);
		console = new Console(glContext().screen, gameState, re);
		gameState.particlePool(new ParticlePool(glContext()));
		networkingSender.init();
		musicPlayer = new MusicPlayer();
		musicPlayer.playBackgroundMusic("/audio/toughened-nomad.mp3");
		fpsText = new TextContent(() -> String.format("FPS: %.1f", fpsCounter.getFPS()), 1000, 20, re.font,
				new ConstraintPair(absolute(20), absolute(20)), 0);
	}

	@Override
	public void update() {
		if (gameState != null) {
			gameState.update();
		}
	}

	@Override
	public void render(float alpha) {
		fpsCounter.update();
		// Render the scene to fbo1
		re.fbo1.render(() -> {
			background(gameState.weather.skyColor(gameState.frameNumber));
			gameState.render(re);
			playerIndicator.render(re, localPlayer.cardPlayer());
			ui.render(re);
		});

		// Render the bright parts of the scene to fbo2
		re.fbo2.render(() -> {
			re.brightnessShaderProgram.use(glContext());
			re.textureRenderer.render(re.fbo1.texture(), new Matrix4f(glContext().screen, glContext()));
		});
//		// Apply Gaussian blur to fbo2 and store in fbo3
//		re.fbo3.bind();
//		re.gaussianBlurShaderProgram.use(glContext());
//		re.gaussianBlurShaderProgram.uniforms().set("horizontal", 1);
//		re.fbo2.texture().bind();
//		re.textureRenderer.render(re.fbo2.texture(), new Matrix4f().translate(-1, -1).scale(2, 2));
//
//		// Apply Gaussian blur to fbo3 and store in fbo2
//		re.fbo2.bind();
//		re.gaussianBlurShaderProgram.uniforms().set("horizontal", 0);
//		re.fbo3.texture().bind();
//		re.textureRenderer.render(re.fbo3.texture(), new Matrix4f().translate(-1, -1).scale(2, 2));
//
//		// Combine the original scene with the blurred bright parts
		DefaultFrameBuffer.instance().render(() -> {
			background(gameState.weather.skyColor(gameState.frameNumber));
			re.textureRenderer.render(re.fbo2.texture(), new Matrix4f(glContext().screen, glContext()));
			console.render(re);
			ruler.render(re);
			if (re.showDebugInfo) {
				fpsText.render(re);
			}
		});
//		re.bloomCombinationShaderProgram.use(glContext());
//		re.fbo1.texture().bind(glContext());
//		re.fbo2.texture().bind(glContext());
//		re.bloomCombinationShaderProgram.uniforms().set("sceneTexture", 0);
//		re.bloomCombinationShaderProgram.uniforms().set("bloomTexture", 1);
//		re.textureRenderer.render(re.fbo1.texture(), new Matrix4f().translate(-1, -1).scale(2, 2));

		ui.particlePool.render(re);
	}

	@Override
	public void cleanUp() {
		System.out.println("Saving game");
//		data.saves().writeGameState(gameState);
		networkingSender.cleanUp();
		musicPlayer.cleanUp();
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
		switch (key) {
			case GLFW_KEY_E:
				localPlayer.cardPlayer().inventory().toggle();
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
				break;
			case GLFW_KEY_K:
				ruler.toggle();
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
		switch (event.button()) {
			case GLFW_MOUSE_BUTTON_LEFT:
				Tile tile = gameState.getMouseHexagon(mouse(), re.camera);
				if (tile != null && tile.actor() instanceof Structure) {
					Structure structure = (Structure) tile.actor();
					structure.maybeInteract(gameState, localPlayer.cardPlayer());
				}
				break;
			case GLFW_MOUSE_BUTTON_RIGHT:
				break;
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
