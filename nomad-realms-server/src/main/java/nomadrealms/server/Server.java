package nomadrealms.server;

import engine.networking.NetworkNode;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.event.networking.handler.ServerSyncedEventHandler;
import nomadrealms.render.ui.custom.console.Console;
import nomadrealms.user.Player;

public class Server {

	private GameState gameState;
	private final Queue<InputEvent> uiEventChannel = new ArrayDeque<>();

	private final NetworkNode networkNode = new NetworkNode();
	private ServerSyncedEventHandler eventHandler;

	private final List<Player> onlinePlayers = new CopyOnWriteArrayList<>();

	private Console console;

	public void initGameState() {
		gameState = new GameState("Server World", uiEventChannel, new OverworldGenerationStrategy(123456789));
	}

	/**
	 * Initializes networking. Should be called after the state is initialized. The console may be null for
	 * headless operation, in which case no log output is displayed.
	 *
	 * @param console the console to log to, may be null
	 */
	public void initNetwork(Console console) {
		this.console = console;
		networkNode.init(44999);
		eventHandler = new ServerSyncedEventHandler(networkNode, onlinePlayers, console);
	}

	public void update() {
		if (gameState == null || eventHandler == null) {
			return;
		}
		gameState.update(new InputEventFrame(gameState.frameNumber));
		while (!uiEventChannel.isEmpty()) {
			uiEventChannel.poll();
		}
		networkNode.update(eventHandler::handle);
	}

	public void cleanUp() {
		networkNode.cleanUp();
	}

	public GameState gameState() {
		return gameState;
	}

	public NetworkNode networkNode() {
		return networkNode;
	}

	public ServerSyncedEventHandler eventHandler() {
		return eventHandler;
	}

	public List<Player> onlinePlayers() {
		return onlinePlayers;
	}

	public Console console() {
		return console;
	}

	public Queue<InputEvent> uiEventChannel() {
		return uiEventChannel;
	}

}
