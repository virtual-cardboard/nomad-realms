package engine.common.loader;

/**
 * A loader for loading strings from files.
 *
 * @author Lunkle
 */
public class StringLoader extends ResourceLoader<String> {

	public StringLoader(String path) {
		super(path);
	}

	public static String loadString(String path) {
		StringLoader stringLoader = new StringLoader(path);
		return stringLoader.load();
	}

	@Override
	public String load() {
		return ResourceLoader.loadString(getPath());
	}

}
