package engine.common.loader;

/**
 * A generic loader interface for loading resources of type T.
 *
 * @param <T> The type of resource to be loaded.
 * @author Lunkle
 */
public interface Loader<T> {

	T load();

}
