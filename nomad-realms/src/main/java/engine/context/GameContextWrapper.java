package engine.context;

import engine.context.input.Mouse;
import engine.nengen.NengenConfiguration;
import engine.visuals.lwjgl.GLContext;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A container for a game context to make switching game contexts thread-safe.
 * <p>
 * Without a wrapper, swapping contexts requires updating every reference to the context. Aside from being cumbersome,
 * concurrency problems can occur because reference swapping does not happen atomically across multiple references.
 * <p>
 * With the context wrapper, swapping contexts becomes thread-safe. Every access to the context is done through a
 * reference to the wrapper, so swapping contexts is instantaneous with one single reference update.
 *
 * @author Jay, Lunkle
 */
public class GameContextWrapper {

	/**
	 * Context that is being wrapped.
	 */
	private GameContext context;

	private final GLContext glContext;

	private final NengenConfiguration config;

	private final Mouse mouse = new Mouse();

	/**
	 * This read-write lock is not a lock on the context itself. The read and write lock is on the accessibility of the
	 * context reference.
	 */
	private final ReadWriteLock contextLock = new ReentrantReadWriteLock();

	/**
	 * Creates a new <code>GameContextWrapper</code> with the given <code>context</code>.
	 *
	 * @param context the context to wrap
	 */
	public GameContextWrapper(GameContext context, GLContext glContext, NengenConfiguration config) {
		setContext(context);
		this.glContext = glContext;
		this.config = config;
	}

	/**
	 * Links the context to this wrapper. Also sets the wrapper for the context.
	 *
	 * @param context
	 */
	public void setContext(GameContext context) {
		context.setWrapper(this);
		this.context = context;
	}

	/**
	 * Returns the context that is being wrapped.
	 *
	 * @return the context that is being wrapped
	 */
	public GameContext context() {
		contextLock.readLock().lock();
		try {
			return context;
		} finally {
			contextLock.readLock().unlock();
		}
	}

	public GLContext glContext() {
		return glContext;
	}

	public NengenConfiguration config() {
		return config;
	}

	public Mouse mouse() {
		return mouse;
	}

}
