package nomadrealms.server;

import java.net.BindException;
import java.util.function.Consumer;
import nomadrealms.server.monitor.ServerMonitor;

/**
 * The entrypoint of the headless server. The server runs until the process is terminated. An embedded web monitor
 * is started when the environment variable <code>NOMAD_MONITOR_PORT</code> (see {@link ServerMonitor#PORT_ENV}) is
 * set to the HTTP port the monitor should serve on.
 *
 * @author Lunkle
 */
public class ServerMainHeadless {

	public static void main(String[] args) {
		Server server = new Server();
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
