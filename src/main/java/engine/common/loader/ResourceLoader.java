package engine.common.loader;

import java.io.InputStream;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * A generic loader for loading resources.
 *
 * @param <T> The type of resource to be loaded.
 * @author Lunkle
 */
public abstract class ResourceLoader<T> implements Loader<T> {

	private final String path;

	public ResourceLoader(String path) {
		this.path = path;
	}

	public abstract T load();

	public String getPath() {
		return path;
	}

	public static InputStream getStream(String path) {
		return requireNonNull(ResourceLoader.class.getResourceAsStream(path), "Could not find resource: " + path);
	}

	public static String loadString(String path) {
		try (Scanner scanner = new Scanner(getStream(path), UTF_8.name())) {
			return scanner.useDelimiter("\\A").next();
		}
	}

}
