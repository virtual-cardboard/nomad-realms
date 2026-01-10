package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;

import java.util.LinkedList;
import java.util.List;
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
import nomadrealms.context.game.world.map.generation.FileBasedGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.particle.PillParticle;
import nomadrealms.render.particle.context.home.HomeScreenFloatingParticle;
import nomadrealms.render.ui.custom.home.HomeInterface;
import nomadrealms.user.data.GameData;

public class HomeScreenContext extends GameContext {

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
			transition(new DeckEditingContext());
		});
		homeInterface.initLoadGameButton(() -> {
			// TODO - Show a list of save files to choose from
			List<Supplier<GameState>> gameStates = data.saves().fetch();
			if (gameStates.isEmpty()) {
				transition(new MainContext());
			} else {
				gameState = gameStates.get(0).get();
				transition(new MainContext(gameState));
			}
		});
		homeInterface.initSandboxButton(() -> {
			transition(new CardSandboxContext());
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
		background(rgb(100, 100, 100));
		gameState.world.renderMap(re);
		particlePool.render(re);
		homeInterface.render(re);
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
		for (int i = 0; i < 100; i++) {
			particlePool.addParticle(
					new PillParticle(glContext(), event.mouse().coordinate().x(), event.mouse().coordinate().y()));
		}
		inputCallbackRegistry.triggerOnPress(event);
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		inputCallbackRegistry.triggerOnDrop(event);
	}

}