package engine.common.loader;

import java.io.File;

/**
 * A generic loader for loading resources from files.
 *
 * @param <T> The type of resource to be loaded.
 * @author Lunkle
 */
public abstract class FileLoader<T> implements Loader<T> {

	private final File file;

	public FileLoader(File file) {
		this.file = file;
	}

	public abstract T load();

	public File getFile() {
		return file;
	}

}
