package nomadrealms.context.game.card;

import static nomadrealms.context.game.card.CardType.ACTION;
import static nomadrealms.context.game.card.CardType.STRUCTURE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import static org.lwjgl.system.MemoryUtil.NULL;

import engine.common.math.Vector2i;
import engine.context.input.Mouse;
import engine.nengen.Nengen;
import engine.nengen.NengenConfiguration;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.Texture;
import nomadrealms.render.RenderingEnvironment;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

public class GameCardTest {

	@Test
	public void testCardTypes() {
		assertEquals(ACTION, GameCard.DASH.type());
		assertEquals(ACTION, GameCard.ATTACK.type());
		assertEquals(STRUCTURE, GameCard.CREATE_ROCK.type());
		assertEquals(STRUCTURE, GameCard.ELECTROSTATIC_ZAPPER.type());
		assertEquals(STRUCTURE, GameCard.WOODEN_CHEST.type());
		assertEquals(ACTION, GameCard.REST.type());
	}

	@Test
	public void testCardArtworkInRenderingEnvironment() {
		Assumptions.assumeTrue(glfwInit(), "GLFW could not be initialized (likely headless environment without display).");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, 0);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, 1);

		long window = glfwCreateWindow(100, 100, "Artwork Test", NULL, NULL);
		if (window == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create GLFW window for card artwork test");
		}

		glfwMakeContextCurrent(window);
		createCapabilities();

		GLContext glContext = new GLContext();
		glContext.setWindowDim(new Vector2i(100, 100));
		glContext.setFbDim(new Vector2i(100, 100));

		Nengen nengen = new Nengen();
		NengenConfiguration config = nengen.configure();
		Mouse mouse = new Mouse();

		RenderingEnvironment re = new RenderingEnvironment(glContext, config, mouse);

		for (GameCard card : GameCard.values()) {
			String artworkKey = card.artwork();
			assertTrue(re.imageMap.containsKey(artworkKey),
					"RenderingEnvironment.imageMap missing key for card " + card.name() + " artwork: '" + artworkKey + "'");
			Texture texture = re.imageMap.get(artworkKey);
			assertNotNull(texture,
					"Texture mapped for card " + card.name() + " artwork '" + artworkKey + "' is null");
		}

		glfwDestroyWindow(window);
		glfwTerminate();
	}

}
