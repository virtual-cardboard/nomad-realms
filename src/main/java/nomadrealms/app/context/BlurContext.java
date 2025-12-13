package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.LinkedList;

import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.world.map.generation.FileBasedGenerationStrategy;
import nomadrealms.context.home.particles.HomeScreenFloatingParticle;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.ui.custom.home.HomeInterface;
import nomadrealms.user.data.GameData;

public class BlurContext extends GameContext {

	private RenderingEnvironment re;

	private final GameData data = new GameData();
	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	private GameState gameState;
	private HomeInterface homeInterface;
	private ParticlePool particlePool;
	private int frameCounter = 0;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());
		gameState = new GameState("Main Menu", new LinkedList<>(), new FileBasedGenerationStrategy());
		homeInterface = new HomeInterface(re, glContext(), inputCallbackRegistry);
		homeInterface.initStartGameButton(() -> {
			transition(new MainContext(data));
		});
		particlePool = new ParticlePool(glContext().screen);
	}

	@Override
	public void update() {
		frameCounter++;
		if (particlePool != null && frameCounter % 5 == 0) {
			for (int i = 0; i < 200; i++) {
				particlePool.addParticle(new HomeScreenFloatingParticle(glContext()));
			}
		}
	}

	@Override
	public void render(float alpha) {
		// 1. Bind FBO
		re.fbo1.bind();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// 2. Render Scene to FBO
		background(rgb(100, 100, 100));
		gameState.world.renderMap(re);
		particlePool.render(re);
		homeInterface.render(re);

		// 3. Unbind FBO (bind default framebuffer)
		DefaultFrameBuffer.instance().bind();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// 4. Render FBO texture to screen with Blur Shader
		re.textureRenderer.render(re.fbo1.texture(), 0, 0, glContext().width(), glContext().height(), re.blurShaderProgram);
	}

	@Override
	public void cleanUp() {
	}

	@Override
	public void input(KeyPressedInputEvent event) {
	}

	@Override
	public void input(KeyReleasedInputEvent event) {
	}

	@Override
	public void input(MouseScrolledInputEvent event) {
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
