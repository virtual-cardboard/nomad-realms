package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateSerializerTest {

	private GameState gameState;

	@BeforeEach
	public void setUp() {
		gameState = new GameState();
	}

	@Test
	public void testSerializeAndDeserialize() {
		// Serialize the game state
		byte[] bytes = GameStateDerializer.serialize(gameState);

		// Deserialize the game state
		GameState loadedGameState = GameStateDerializer.deserialize(bytes);

		// Verify the deserialized game state
		assertNotNull(loadedGameState);
		assertEquals(gameState.frameNumber, loadedGameState.frameNumber);
		assertEquals(gameState.showMap, loadedGameState.showMap);
		assertEquals(gameState.uiEventChannel.size(), loadedGameState.uiEventChannel.size());
		assertEquals(gameState.inputFrames.size(), loadedGameState.inputFrames.size());
	}

}
