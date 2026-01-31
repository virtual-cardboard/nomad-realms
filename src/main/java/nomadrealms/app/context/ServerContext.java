package nomadrealms.app.context;

import java.util.ArrayDeque;
import java.util.Queue;

import engine.context.GameContext;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;

public class ServerContext extends GameContext {

	private RenderingEnvironment re;
	private GameState gameState;
	private final Queue<InputEvent> uiEventChannel = new ArrayDeque<>();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		gameState = new GameState("Server World", uiEventChannel, new OverworldGenerationStrategy(123456789));
	}

	@Override
	public void update() {
		gameState.update();
		while (!uiEventChannel.isEmpty()) {
			uiEventChannel.poll();
		}
	}

	@Override
	public void render(float alpha) {
		background(gameState.weather.skyColor(gameState.frameNumber));
		gameState.render(re);
	}

}
