package nomadrealms.server;

import java.net.BindException;
import java.nio.file.Paths;
import java.util.function.Consumer;
import nomadrealms.context.game.GameState;
import nomadrealms.server.monitor.ServerMonitor;
import nomadrealms.server.storage.LocalSaveStorage;

/**
 * The entrypoint of the headless server. The server runs until the process is terminated. An embedded web monitor
 * is started when the environment variable <code>NOMAD_MONITOR_PORT</code> (see {@link ServerMonitor#PORT_ENV}) is
 * set to the HTTP port the monitor should serve on. Saves live in the directory given by
 * <code>NOMAD_SAVES_DIR</code>, defaulting to <code>saves/</code> relative to the working directory. A save is
 * loaded at boot instead of generating a fresh world when <code>NOMAD_WORLD</code> is set to the name of an
 * existing save.
 *
 * @author Lunkle
 */
public class ServerMainHeadless {

	/** The name of the environment variable that overrides the save storage directory. */
	public static final String SAVES_DIR_ENV = "NOMAD_SAVES_DIR";

	/** The name of the environment variable that boots the server from a save instead of a fresh world. */
	public static final String WORLD_ENV = "NOMAD_WORLD";

	public static void main(String[] args) {
		Server server = new Server();
		configureSaveStorage(server);
		ServerMonitor monitor = startMonitor(server);
		Consumer<String> logger = monitor == null ? null : monitor.logger();
		Thread updaterThread = new Thread(new HeadlessServerUpdater(server, 10, logger));
		updaterThread.setName("Headless Server Updater Thread");
		updaterThread.start();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			server.cleanUp();
			if (monitor != null) {
				monitor.stop();
			}
		}));
	}

	/**
	 * Points the server at the save storage directory from the environment, and boots from a save when
	 * <code>NOMAD_WORLD</code> names an existing save.
	 *
	 * @param server the server to configure
	 */
	private static void configureSaveStorage(Server server) {
		String savesDir = System.getenv(SAVES_DIR_ENV);
		if (savesDir != null && !savesDir.isEmpty()) {
			server.saveStorage(new LocalSaveStorage(Paths.get(savesDir)));
		} else {
			server.saveStorage(new LocalSaveStorage(Paths.get("saves")));
		}
		String world = System.getenv(WORLD_ENV);
		if (world != null && !world.isEmpty()) {
			GameState loaded = server.load(world);
			if (loaded == null) {
				System.out.println("No save found under '" + world + "', generating a fresh world.");
			} else {
				System.out.println("Loaded world '" + world + "' from save.");
			}
		}
	}

	/**
	 * Starts the monitor if the monitor port environment variable is set, and returns it. Prints a warning instead
	 * of failing if the port is taken.
	 *
	 * @param server the server to monitor
	 * @return the started monitor, or null if the monitor is disabled
	 */
	private static ServerMonitor startMonitor(Server server) {
		String portProperty = System.getenv(ServerMonitor.PORT_ENV);
		if (portProperty == null || portProperty.isEmpty()) {
			return null;
		}
		ServerMonitor monitor = new ServerMonitor(server, Integer.parseInt(portProperty));
		try {
			monitor.start();
		} catch (Exception e) {
			if (e.getCause() instanceof BindException) {
				System.out.println("Monitor could not bind to port " + portProperty + ", monitor disabled.");
				return null;
			}
			throw e;
		}
		System.out.println("Monitor serving on http://localhost:" + portProperty);
		return monitor;
	}

}
