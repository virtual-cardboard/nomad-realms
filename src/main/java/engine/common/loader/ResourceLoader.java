package engine.common.loader;

import java.io.InputStream;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class ResourceLoader {

	public static InputStream getStream(String path) {
		return requireNonNull(ResourceLoader.class.getResourceAsStream(path), "Could not find resource: " + path);
	}

	public static String loadString(String path) {
		try (Scanner scanner = new Scanner(getStream(path), UTF_8.name())) {
			return scanner.useDelimiter("\\A").next();
		}
	}

}
