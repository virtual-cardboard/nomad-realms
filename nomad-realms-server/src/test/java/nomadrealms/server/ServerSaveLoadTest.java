package nomadrealms.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.file.Path;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.server.storage.LocalSaveStorage;
import nomadrealms.server.storage.SaveMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ServerSaveLoadTest {

	@TempDir
	Path savesDirectory;

	@Test
	public void testSaveAndLoadRoundTrip() {
		Server server = new Server();
		server.saveStorage(new LocalSaveStorage(savesDirectory));
		server.initGameState();

		server.requestSave("Server World");
		server.update(); // The save runs at the end of the tick on the tick thread.
		long frameNumber = server.gameState().frameNumber;

		List<SaveMetadata> saves = server.saveStorage().list();
		assertEquals(1, saves.size());
		assertEquals("Server World", saves.get(0).worldName());

		GameState reloaded = server.load("Server World");
		assertNotNull(reloaded);
		assertEquals(frameNumber, reloaded.frameNumber);
	}

	@Test
	public void testLoadMissingSaveKeepsFreshState() {
		Server server = new Server();
		server.saveStorage(new LocalSaveStorage(savesDirectory));
		server.initGameState();

		assertNull(server.load("Missing World"));
		assertNotNull(server.gameState());
	}

}
