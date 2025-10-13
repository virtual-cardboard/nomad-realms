package engine.context;

import static engine.nengen.EngineConfiguration.DEBUG;

import engine.common.time.TimestepTimer;
import engine.nengen.EngineConfiguration;

public class GameTickUpdater extends TimestepTimer implements Runnable {

	private final GameContextWrapper wrapper;
	private final EngineConfiguration configuration;

	/**
	 * Creates a new {@link GameTickUpdater} with the given configuration and context wrapper.
	 *
	 * @param configuration The configuration for the engine.
	 * @param wrapper       The context wrapper.
	 */
	public GameTickUpdater(EngineConfiguration configuration, GameContextWrapper wrapper) {
		super(1000 / configuration.tickRate());
		this.configuration = configuration;
		this.wrapper = wrapper;
	}

	@Override
	protected void startActions() {
		DEBUG("Initializing tick updater...");
	}

	@Override
	protected void update() {
		GameContext context = wrapper.context();
		context.update();
	}

	@Override
	protected void endActions() {
		DEBUG("Ending tick updater...");
		System.out.println("Tick updater ended.");
	}

	@Override
	protected boolean endCondition() {
		return configuration.shouldClose();
	}

}
