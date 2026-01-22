package engine.common.loader;

/**
 * A loader for loading strings from files.
 *
 * @author Lunkle
 */
public class StringLoader extends FileLoader<String> {

	private final String path;

	public StringLoader(String path) {
		this.path = path;
	}

	public static String loadString(String path) {
		StringLoader stringLoader = new StringLoader(path);
		return stringLoader.load();
	}

	@Override
	public String load() {
		return ResourceLoader.loadString(path);
	}

}
