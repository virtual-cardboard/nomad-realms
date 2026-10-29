package nomadrealms.server.storage;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link SaveStorage} that keeps saves on the local filesystem in a given directory, one file per world named
 * <code>&lt;worldName&gt;.save</code>. Saves are written atomically: the blob first lands in a temporary file which
 * is then moved over the target, so a crashed server never leaves a half-written save behind.
 *
 * @author Lunkle
 */
public class LocalSaveStorage extends SaveStorage {

	private final Path directory;

	/**
	 * Creates a new <code>LocalSaveStorage</code> in the given directory. The directory is created if it does not
	 * exist.
	 *
	 * @param directory the directory that holds the save files
	 */
	public LocalSaveStorage(Path directory) {
		this.directory = directory;
		try {
			Files.createDirectories(directory);
		} catch (IOException e) {
			throw new RuntimeException("Failed to create save directory: " + directory.toAbsolutePath(), e);
		}
	}

	/** @return the directory that holds the save files */
	public Path directory() {
		return directory;
	}

	private Path file(String worldName) {
		return directory.resolve(worldName + ".save");
	}

	private Path temporaryFile(String worldName) {
		return directory.resolve(worldName + ".save.tmp");
	}

	@Override
	public void save(String worldName, byte[] data) {
		Path file = file(worldName);
		Path temporary = temporaryFile(worldName);
		try {
			Files.write(temporary, data);
			try {
				Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (AtomicMoveNotSupportedException e) {
				Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to save " + worldName + " to " + directory.toAbsolutePath(), e);
		}
	}

	@Override
	public byte[] load(String worldName) {
		try {
			Path file = file(worldName);
			if (!Files.exists(file)) {
				return null;
			}
			return Files.readAllBytes(file);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load " + worldName + " from " + directory.toAbsolutePath(), e);
		}
	}

	@Override
	public List<SaveMetadata> list() {
		List<SaveMetadata> metadata = new ArrayList<>();
		try {
			for (Path file : Files.newDirectoryStream(directory, "*.save")) {
				metadata.add(new SaveMetadata(
						nameOf(file),
						Files.size(file),
						Files.getLastModifiedTime(file).toMillis()
				));
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to list saves in " + directory.toAbsolutePath(), e);
		}
		return metadata;
	}

	private String nameOf(Path file) {
		String fileName = file.getFileName().toString();
		return fileName.substring(0, fileName.length() - ".save".length());
	}

	@Override
	public boolean delete(String worldName) {
		try {
			return Files.deleteIfExists(file(worldName));
		} catch (IOException e) {
			throw new RuntimeException("Failed to delete " + worldName + " in " + directory.toAbsolutePath(), e);
		}
	}

}
