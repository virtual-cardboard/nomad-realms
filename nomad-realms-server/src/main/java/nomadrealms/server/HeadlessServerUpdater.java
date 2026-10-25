package nomadrealms.server;

import engine.common.time.TimestepTimer;
import java.util.function.Consumer;

/**
 * A {@link TimestepTimer} that ticks a {@link Server} at a fixed rate without a display. This is the headless
 * counterpart of the tick half of the Nengen engine.
 *
 * @author Lunkle
 */
public class HeadlessServerUpdater extends TimestepTimer implements Runnable {

	private final Server server;
	private final Consumer<String> logger;

	/**
	 * Creates a new <code>HeadlessServerUpdater</code> that ticks the given <code>server</code> at the given tick
	 * rate.
	 *
	 * @param server   the server to tick
	 * @param tickRate the number of ticks per second
	 */
	public HeadlessServerUpdater(Server server, int tickRate) {
		this(server, tickRate, null);
	}

	/**
	 * Creates a new <code>HeadlessServerUpdater</code> that ticks the given <code>server</code> at the given tick
	 * rate, logging network events through the given logger. The logger may be null or already combine several
	 * consumers, e.g. standard output and a {@link nomadrealms.server.monitor.ServerMonitor}.
	 *
	 * @param server   the server to tick
	 * @param tickRate the number of ticks per second
	 * @param logger   the consumer that receives network log messages, may be null for standard output
	 */
	public HeadlessServerUpdater(Server server, int tickRate, Consumer<String> logger) {
		super(1000 / tickRate);
		this.server = server;
		this.logger = logger;
	}

	@Override
	protected void startActions() {
		server.initGameState();
		server.initNetwork(logger == null ? System.out::println : logger);
		System.out.println("Headless server started on UDP port " + server.networkNode().port());
	}

	@Override
	protected void update() {
		server.update();
	}

	@Override
	protected boolean endCondition() {
		return false; // The server runs until the process is terminated.
	}

	@Override
	protected void endActions() {
		System.out.println("Headless server ended.");
		server.cleanUp();
	}

}
