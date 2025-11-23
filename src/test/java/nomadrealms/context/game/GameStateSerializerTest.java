package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateSerializerTest {

	private GameState gameState;
	private final String filePath = "test_gamestate.sav";

	@BeforeEach
	public void setUp() {
		gameState = new GameState();
	}

	@Test
	public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
		GameStateSerializer serializer = new GameStateSerializer();

		// Serialize the game state
		serializer.serialize(gameState, filePath);

		// Deserialize the game state
		GameState loadedGameState = serializer.deserialize(filePath);

		// Verify the deserialized game state
		assertNotNull(loadedGameState);
		assertEquals(gameState.frameNumber, loadedGameState.frameNumber);
		assertEquals(gameState.showMap, loadedGameState.showMap);
		assertEquals(gameState.uiEventChannel.size(), loadedGameState.uiEventChannel.size());
		assertEquals(gameState.inputFrames.size(), loadedGameState.inputFrames.size());

		// Clean up the test file
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

}
