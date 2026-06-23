package nomadrealms.render;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.system.MemoryUtil.NULL;

import engine.common.math.Vector2i;
import engine.context.GameContext;
import engine.context.GameContextWrapper;
import engine.nengen.Nengen;
import engine.nengen.NengenConfiguration;
import engine.visuals.lwjgl.GLContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;
import nomadrealms.app.context.MainContext;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.world.weather.Clouds;
import nomadrealms.context.game.zone.BeginnerDecks;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.math.generation.map.OpenSimplexNoise;
import org.junit.jupiter.api.Test;

public class ScreenshotTest {

	private static final int WIDTH = 1200;
	private static final int HEIGHT = 900;
	private static final String GOLDEN_PATH = "src/test/resources/golden/overworld.png";

	@Test
	public void testOverworldScreenshot() throws Exception {
		if (!glfwInit()) {
			throw new RuntimeException("Failed to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		long window = glfwCreateWindow(WIDTH, HEIGHT, "Screenshot Test", NULL, NULL);
		if (window == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create GLFW window");
		}

		glfwMakeContextCurrent(window);
		createCapabilities();

		glEnable(GL_BLEND);
		glEnable(GL_MULTISAMPLE);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

		GLContext glContext = new GLContext();
		glContext.setWindowDim(new Vector2i(WIDTH, HEIGHT));
		glContext.setFbDim(new Vector2i(WIDTH, HEIGHT));

		Nengen nengen = new Nengen();
		NengenConfiguration config = nengen.configure();
		config.setWindowDim(WIDTH, HEIGHT);

		MainContext mainContext = new MainContext(
				BeginnerDecks.RUNNING_AND_WALKING.deckList().toDeck(),
				BeginnerDecks.AGRICULTURE_AND_LABOUR.deckList().toDeck(),
				BeginnerDecks.CYCLE_AND_SEARCH.deckList().toDeck(),
				BeginnerDecks.PUNCH_AND_GRAPPLE.deckList().toDeck()
		);

		GameContextWrapper wrapper = new GameContextWrapper(mainContext, glContext, config);

		Method doInit = mainContext.getClass().getSuperclass().getDeclaredMethod("doInit");
		doInit.setAccessible(true);
		doInit.invoke(mainContext);

		// Make clouds deterministic for the test
		Field gameStateField = MainContext.class.getDeclaredField("gameState");
		gameStateField.setAccessible(true);
		GameState gameState = (GameState) gameStateField.get(mainContext);
		Clouds clouds = gameState.clouds;
		Field noiseField = Clouds.class.getDeclaredField("noise");
		noiseField.setAccessible(true);
		noiseField.set(clouds, new OpenSimplexNoise(123456789L));

		// Set camera zoom to a smaller number as requested
		mainContext.renderEnvironment().is.camera.zoom(0.5f);

		Method update = mainContext.getClass().getSuperclass().getDeclaredMethod("update");
		update.setAccessible(true);
		update.invoke(mainContext);

		Method render = mainContext.getClass().getSuperclass().getDeclaredMethod("render", float.class);
		render.setAccessible(true);
		render.invoke(mainContext, 1.0f);

		BufferedImage captured = ScreenshotUtility.capture(WIDTH, HEIGHT);

		File goldenFile = new File(GOLDEN_PATH);
		if (!goldenFile.exists()) {
			System.out.println("Golden image not found. Saving captured image as golden.");
			ScreenshotUtility.saveImage(captured, GOLDEN_PATH);
		} else {
			BufferedImage golden = ScreenshotUtility.loadImage(GOLDEN_PATH);
			double difference = ScreenshotUtility.compare(captured, golden, 5);
			System.out.println("Pixel difference: " + difference + "%");
			assertTrue(difference <= 2.0, "Screenshot differs from golden image by " + difference + "%");
		}

		glfwDestroyWindow(window);
		glfwTerminate();
	}
}
