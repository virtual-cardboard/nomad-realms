package nomadrealms.user.data;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Supplier;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.GameStateSerializer;

/**
 * A data container for all saved game states. Includes methods for reading and writing saves.
 *
 * @author Lunkle
 */
public class SavesData {

	private final File saveDirectory;

	private transient List<File> cachedSaveFiles = null;
	private transient List<GameState> cachedGameStates = null;

	private GameStateSerializer serializer = new GameStateSerializer();

	public SavesData(String userHome) {
		saveDirectory = Paths.get(userHome, GameData.STORAGE_FOLDER, "saves").toFile();
		if (!saveDirectory.exists()) {
			if (!saveDirectory.mkdirs()) {
				throw new RuntimeException("Failed to create save directory: " + saveDirectory.getAbsolutePath());
			}
		}
	}

	private List<File> readSaveFiles() {
		return asList(requireNonNull(saveDirectory.listFiles()));
	}

	private GameState readSave(File file) {
		if (file == null || !file.exists() || !file.isFile() || !cachedSaveFiles.contains(file)) {
			throw new IllegalArgumentException("Save file does not exist: " + file);
		}
		GameState state;
		try {
			state = serializer.deserialize(file.getAbsolutePath());
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return state;
	}

	/**
	 * Fetches all saved game states as suppliers to allow for lazy loading.
	 *
	 * @return A list of suppliers for each saved game state.
	 */
	public List<Supplier<GameState>> fetch() {
		if (cachedSaveFiles == null) {
			cachedSaveFiles = readSaveFiles();
		}
		return cachedSaveFiles.stream().map(file -> (Supplier<GameState>) () -> readSave(file)).collect(toList());
	}

	/**
	 * Writes the given game state to disk.
	 *
	 * @param gameState The game state to save.
	 */
	public void writeGameState(GameState gameState) {
		File file = new File(saveDirectory, gameState.name() + ".save");
		try {
			serializer.serialize(gameState, file.getAbsolutePath());
			cachedSaveFiles = null; // Invalidate cache
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
