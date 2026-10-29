package nomadrealms.server;

import engine.networking.NetworkNode;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.GameStateDerializer;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.event.networking.handler.ServerSyncedEventHandler;
import nomadrealms.server.storage.LocalSaveStorage;
import nomadrealms.server.storage.SaveStorage;
import nomadrealms.user.Player;

/**
 * The core of the networked game. Owns the game state, the networking, and the online players. This class is
 * completely decoupled from any display; logging is done through the given logger consumer.
 * <p>
 * Saving goes through a {@link SaveStorage}, defaulting to the local filesystem in <code>saves/</code>. Save
 * requests are queued and drained at the end of {@link #update()}, so serialization happens on the single tick
 * thread and can never race world mutation.
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
	private SaveStorage saveStorage = new LocalSaveStorage(Paths.get("saves"));
	private String pendingSaveName;

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

	/**
	 * Sets the storage that saves are written to and loaded from. Should be called before the state is initialized.
	 *
	 * @param saveStorage the storage to use for saves
	 */
	public void saveStorage(SaveStorage saveStorage) {
		this.saveStorage = saveStorage;
	}

	/** @return the storage that saves are written to and loaded from, never null */
	public SaveStorage saveStorage() {
		return saveStorage;
	}

	/**
	 * Serializes and saves the current game state into the save storage. Only safe on the thread that owns the game
	 * state (the tick thread); {@link #requestSave(String)} queues a save to run there.
	 *
	 * @param worldName the name to save the world under
	 */
	public void save(String worldName) {
		if (gameState == null || saveStorage == null) {
			return;
		}
		if (logger != null) {
			logger.accept("Saving world " + worldName);
		}
		saveStorage.save(worldName, GameStateDerializer.serialize(gameState));
	}

	/**
	 * Queues a save of the current game state under the given name to run at the end of the next tick. Returns
	 * immediately; the last requested name wins.
	 *
	 * @param worldName the name to save the world under
	 */
	public void requestSave(String worldName) {
		this.pendingSaveName = worldName;
	}

	/**
	 * Loads the game state saved under the given name, replacing the current one. Only safe before ticking starts.
	 *
	 * @param worldName the name of the world to load
	 * @return the loaded game state, or null if there is no save under the given name
	 */
	public GameState load(String worldName) {
		byte[] data = saveStorage.load(worldName);
		if (data == null) {
			return null;
		}
		gameState = GameStateDerializer.deserialize(data);
		return gameState;
	}

	public void update() {
		if (gameState == null) {
			return;
		}
		gameState.update(new InputEventFrame(gameState.frameNumber));
		while (!uiEventChannel.isEmpty()) {
			uiEventChannel.poll();
		}
		if (pendingSaveName != null) {
			save(pendingSaveName);
			pendingSaveName = null;
		}
		if (eventHandler == null) {
			return;
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
