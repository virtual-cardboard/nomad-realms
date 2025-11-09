package engine.common.loader;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.File;
import java.io.IOException;

/**
 * A loader for loading strings from files.
 *
 * @author Lunkle
 */
public class StringLoader extends FileLoader<String> {

	public StringLoader(File file) {
		super(file);
	}

	public static String loadString(File file) {
		StringLoader stringLoader = new StringLoader(file);
		return stringLoader.load();
	}

	@Override
	public String load() {
		try {
			return new String(readAllBytes(get(getFile().getAbsolutePath())));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
