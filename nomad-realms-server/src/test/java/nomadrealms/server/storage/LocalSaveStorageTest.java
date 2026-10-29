package nomadrealms.server.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class LocalSaveStorageTest {

	@TempDir
	Path directory;

	@Test
	public void testSaveLoadDeleteRoundTrip() {
		LocalSaveStorage storage = new LocalSaveStorage(directory);
		byte[] data = {1, 2, 3, 4, 5};

		storage.save("Server World", data);
		assertTrue(storage.load("Server World") != null && data[0] == storage.load("Server World")[0]
				&& data[4] == storage.load("Server World")[4], "loaded bytes should round trip");

		SaveMetadata metadata = storage.list().get(0);
		assertEquals("Server World", metadata.worldName());
		assertEquals(5, metadata.sizeBytes());
		assertTrue(metadata.lastModifiedMillis() > 0);

		assertTrue(storage.delete("Server World"));
		assertFalse(storage.delete("Server World"));
		assertNull(storage.load("Server World"));
	}

	@Test
	public void testOverwriteReplacesSave() {
		LocalSaveStorage storage = new LocalSaveStorage(directory);

		storage.save("World", new byte[] {1});
		storage.save("World", new byte[] {1, 2, 3});

		List<SaveMetadata> saves = storage.list();
		assertEquals(1, saves.size(), "overwriting should not create a second save");
		assertEquals(3, saves.get(0).sizeBytes());
		assertNotNull(storage.load("World"));
		assertEquals(3, storage.load("World").length);
	}

	@Test
	public void testMissingSaveLoadsNull() {
		LocalSaveStorage storage = new LocalSaveStorage(directory);
		assertNull(storage.load("Does Not Exist"));
		assertEquals(0, storage.list().size());
	}

}
