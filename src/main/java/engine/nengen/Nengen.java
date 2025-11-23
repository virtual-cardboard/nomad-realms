package engine.nengen;

import engine.context.GameContext;
import engine.context.GameContextWrapper;
import engine.context.GameTickUpdater;
import engine.context.GameWindowUpdater;
import engine.visuals.lwjgl.GLContext;

/**
 * The main class for the Nengen game engine.
 * <br>
 * <br>
 * To use Nengen, create a new instance of this class, configure it, and then call {@link #startNengen(GameContext)}.
 * <br>
 * <br>
 * Example usage:<br>
 * <code>
 * Nengen nengen = new Nengen();<br>nengen.configure()<br>&nbsp;.setWindowDim(800, 600)<br>&nbsp;.setWindowName("My
 * Game")<br>&nbsp;.setFrameRate(60)<br>&nbsp;.setTickRate(20)<br>&nbsp;.setResizable(false)<br>&nbsp;.setFullscreen(false);<br>
 * nengen.startNengen(new MyGameContext());
 * </code>
 *
 * @author Lunkle
 */
public class Nengen {

	/**
	 * External-facing configuration for Nengen. Note that this configuration is converted to an internal-facing
	 * configuration when the engine is started.
	 */
	private final NengenConfiguration configuration;

	public Nengen() {
		configuration = new NengenConfiguration();
	}

	/**
	 * Offers the configuration interface for the Nengen engine.
	 */
	public NengenConfiguration configure() {
		return configuration;
	}

	public void startNengen(GameContext context) {
		assert context != null : "Context cannot be null";
		GLContext glContext = new GLContext();

		EngineConfiguration config = configuration.build();
		GameContextWrapper wrapper = new GameContextWrapper(context, glContext, configuration);

//		Thread renderThread = new Thread(new GameWindowUpdater(config, wrapper, glContext));
//		renderThread.setName("Render Thread");
//		Thread tickThread = new Thread(new GameTickUpdater(config, wrapper));
//		tickThread.setName("Tick Thread");
//		tickThread.start();
//		renderThread.start();

		Runnable renderThread = new GameWindowUpdater(config, wrapper, glContext);
		Thread tickThread = new Thread(new GameTickUpdater(config, wrapper));
		tickThread.setName("Tick Thread");
		tickThread.start();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			wrapper.context().doCleanUp();
		}));
		renderThread.run();
	}

}
