package nomadrealms.app.context;

import engine.context.GameContext;
import engine.networking.NetworkNode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.event.networking.handler.ServerSyncedEventHandler;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.user.Player;

public class ServerContext extends GameContext {

	private RenderingEnvironment re;
	private GameState gameState;
	private final Queue<InputEvent> uiEventChannel = new ArrayDeque<>();

	private final NetworkNode networkNode = new NetworkNode();
	private ServerSyncedEventHandler eventHandler;

	private final List<Player> onlinePlayers = new ArrayList<>();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		gameState = new GameState("Server World", uiEventChannel, new OverworldGenerationStrategy(123456789));
		networkNode.init(44999);
		eventHandler = new ServerSyncedEventHandler(networkNode, onlinePlayers);
	}

	@Override
	public void update() {
		if (gameState == null) {
			return;
		}
		gameState.update(new InputEventFrame(gameState.frameNumber));
		while (!uiEventChannel.isEmpty()) {
			uiEventChannel.poll();
		}
		networkNode.update(eventHandler::handle);
	}

	@Override
	public void cleanUp() {
		networkNode.cleanUp();
	}

	@Override
	public void render(float alpha) {
		background(gameState.weather.skyColor(gameState.frameNumber));
		gameState.render(re);
	}

}
