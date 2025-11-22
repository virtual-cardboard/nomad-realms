package nomadrealms.user.data;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PreferencesData {

	private final Path preferencesDirectory;

	public PreferencesData(String userHome) {
		preferencesDirectory = Paths.get(userHome, GameData.STORAGE_FOLDER, "preferences");
	}

	public Path preferencesDirectory() {
		return preferencesDirectory;
	}


}
