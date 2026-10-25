package nomadrealms.server.monitor;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import nomadrealms.server.Server;

/**
 * An embedded web monitor for a {@link Server}. Exposes recent state through a small HTTP API and live logs through a
 * WebSocket. This allows remotely viewing the state of a headless server, e.g. one running on Render.
 * <p>
 * </p>
 * The monitor is disabled by default; the headless server enables it when the <code>NOMAD_MONITOR_PORT</code>
 * environment variable is set to the desired HTTP port.
 *
 * @author Lunkle
 */
public class ServerMonitor {

	/** The maximum number of log lines kept in memory and replayed on WebSocket connect. */
	private static final int MAX_LOG_HISTORY = 200;

	/** The name of the environment variable that enables the monitor. */
	public static final String PORT_ENV = "NOMAD_MONITOR_PORT";

	private final Server server;
	private final int port;

	private Javalin app;
	private final long startTime = System.currentTimeMillis();

	private final Deque<String> logHistory = new ArrayDeque<>();
	private final Object logLock = new Object();
	private final Map<org.eclipse.jetty.websocket.api.Session, WsContext> logSessions = new ConcurrentHashMap<>();

	/**
	 * Creates a new <code>ServerMonitor</code> for the given <code>server</code>, serving HTTP on the given port.
	 *
	 * @param server the server to monitor
	 * @param port   the HTTP port to serve on
	 */
	public ServerMonitor(Server server, int port) {
		this.server = server;
		this.port = port;
	}

	/**
	 * Returns a logger that sends its messages to the monitor and to standard output, for passing to
	 * <code>Server.initNetwork</code>.
	 *
	 * @return the combined logging consumer
	 */
	public Consumer<String> logger() {
		return message -> {
			log(message);
			System.out.println(message);
		};
	}

	/** Starts the monitor. Does nothing if it is already started. */
	public void start() {
		if (app != null) {
			return;
		}
		app = Javalin.create(config -> config.showJavalinBanner = false).start(port);
		app.get("/api/health", ctx -> ctx.json(health()));
		app.get("/api/players", ctx -> ctx.json(players()));
		app.get("/api/stats", ctx -> ctx.json(stats()));
		app.ws("/ws/logs", ws -> {
			ws.onConnect(ctx -> {
				logSessions.put(ctx.session, ctx);
				for (String line : recentLogs()) {
					ctx.send(line);
				}
			});
			ws.onClose(ctx -> logSessions.remove(ctx.session));
			ws.onError(ctx -> logSessions.remove(ctx.session));
		});
	}

	/** Stops the monitor. Does nothing if it is not started. */
	public void stop() {
		if (app != null) {
			app.stop();
			app = null;
			logSessions.clear();
		}
	}	/**
	 * @return the HTTP port the monitor is actually serving on, resolving port <code>0</code> to the assigned
	 *         ephemeral port once started
	 */
	public int port() {
		return app == null ? port : app.port();
	}

	/**
	 * Records a log line and broadcasts it to connected WebSocket sessions.
	 *
	 * @param message the message to log
	 */
	protected void log(String message) {
		synchronized (logLock) {
			logHistory.addLast(message);
			while (logHistory.size() > MAX_LOG_HISTORY) {
				logHistory.removeFirst();
			}
		}
		for (Map.Entry<org.eclipse.jetty.websocket.api.Session, WsContext> entry : logSessions.entrySet()) {
			WsContext session = entry.getValue();
			try {
				session.send(message);
			} catch (Exception ignored) {
				logSessions.remove(entry.getKey());
			}
		}
	}

	/** @return a snapshot of the most recent log lines, oldest first */
	private List<String> recentLogs() {
		synchronized (logLock) {
			return new ArrayList<>(logHistory);
		}
	}

	/**
	 * @return the health of the server, including whether the state has been initialized and the number of online
	 *         players
	 */
	private Map<String, Object> health() {
		Map<String, Object> health = new LinkedHashMap<>();
		health.put("status", "running");
		health.put("initialized", server.gameState() != null);
		health.put("onlinePlayers", server.onlinePlayers().size());
		return health;
	}

	/** @return the list of online players, with their name and address */
	private List<Map<String, Object>> players() {
		List<Map<String, Object>> players = new ArrayList<>();
		for (nomadrealms.user.Player player : server.onlinePlayers()) {
			Map<String, Object> playerInfo = new LinkedHashMap<>();
			playerInfo.put("name", player.name());
			playerInfo.put("address", String.valueOf(player.address()));
			players.add(playerInfo);
		}
		return players;
	}

	/** @return general statistics about the server */
	private Map<String, Object> stats() {
		Map<String, Object> stats = new LinkedHashMap<>();
		stats.put("frameNumber", server.gameState() == null ? 0 : server.gameState().frameNumber);
		stats.put("onlinePlayers", server.onlinePlayers().size());
		stats.put("uptimeMillis", System.currentTimeMillis() - startTime);
		stats.put("monitorPort", port());
		return stats;
	}

}
