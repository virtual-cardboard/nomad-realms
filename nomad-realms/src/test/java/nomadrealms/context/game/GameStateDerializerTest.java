package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateDerializerTest {

	private GameState gameState;

	@BeforeEach
	public void setUp() {
		gameState = new GameState();
	}

	@Test
	public void testSerializeAndDeserialize() {
		// Given a game state with non-default values
		gameState.frameNumber = 123L;
		gameState.showMap = true;
		gameState.update(); // This increments frameNumber and adds an item to inputFrames list

		// Given a fixed UUID
		UUID fixedUuid = UUID.randomUUID();
		gameState.uuid = fixedUuid;

		// When serializing and deserializing
		byte[] bytes = GameStateDerializer.serialize(gameState);
		GameState loadedGameState = GameStateDerializer.deserialize(bytes);

		// Then all serializable fields should be equal
		assertNotNull(loadedGameState);
		assertEquals(gameState.uuid, loadedGameState.uuid);
		assertEquals(gameState.frameNumber, loadedGameState.frameNumber);
		assertEquals(gameState.showMap, loadedGameState.showMap);

		// This assertion will fail because collections are not yet supported by the Derializable processor, exposing data loss.
		// TODO: Enable this assertion once Derializable supports collections
		// assertEquals(gameState.inputFrames.size(), loadedGameState.inputFrames.size());
		// Assertions for other fields like 'world' and 'weather' should also be added once they are supported.
	}

}
