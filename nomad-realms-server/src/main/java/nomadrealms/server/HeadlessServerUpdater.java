package nomadrealms.server;

import engine.common.time.TimestepTimer;

/**
 * A {@link TimestepTimer} that ticks a {@link Server} at a fixed rate without a display. This is the headless
 * counterpart of the tick half of the Nengen engine.
 *
 * @author Lunkle
 */
public class HeadlessServerUpdater extends TimestepTimer implements Runnable {

	private final Server server;

	/**
	 * Creates a new <code>HeadlessServerUpdater</code> that ticks the given <code>server</code> at the given tick
	 * rate.
	 *
	 * @param server   the server to tick
	 * @param tickRate the number of ticks per second
	 */
	public HeadlessServerUpdater(Server server, int tickRate) {
		super(1000 / tickRate);
		this.server = server;
	}

	@Override
	protected void startActions() {
		server.initGameState();
		server.initNetwork(System.out::println);
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
