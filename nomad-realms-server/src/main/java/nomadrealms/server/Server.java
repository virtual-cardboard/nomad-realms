package nomadrealms.server;

import engine.networking.NetworkNode;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.event.networking.handler.ServerSyncedEventHandler;
import nomadrealms.user.Player;

/**
 * The core of the networked game. Owns the game state, the networking, and the online players. This class is
 * completely decoupled from any display; logging is done through the given logger consumer.
 * <p>
 * The GUI server view is <code>nomadrealms.app.context.ServerContext</code>, and the headless server entrypoint is
 * <code>nomadrealms.server.ServerMainHeadless</code>.
 *
 * @author Lunkle
 */
public class Server {

	/** The port that the server listens on. */
	public static final int PORT = 44999;

	private GameState gameState;
	private final Queue<InputEvent> uiEventChannel = new ArrayDeque<>();

	private final NetworkNode networkNode = new NetworkNode();
	private ServerSyncedEventHandler eventHandler;

	private final List<Player> onlinePlayers = new CopyOnWriteArrayList<>();

	private Consumer<String> logger;

	public void initGameState() {
		gameState = new GameState("Server World", uiEventChannel, new OverworldGenerationStrategy(123456789));
	}

	/**
	 * Initializes networking. Should be called after the state is initialized. The logger may be null for headless
	 * operation that does not want to log network events.
	 *
	 * @param logger the consumer that receives log messages, may be null
	 */
	public void initNetwork(Consumer<String> logger) {
		this.logger = logger;
		networkNode.init(PORT);
		eventHandler = new ServerSyncedEventHandler(networkNode, onlinePlayers, logger);
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

	public List<Player> onlinePlayers() {
		return onlinePlayers;
	}

}
