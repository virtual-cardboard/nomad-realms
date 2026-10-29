package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nomadrealms.context.game.event.InputEventFrame;
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
		gameState.update(new InputEventFrame(gameState.frameNumber)); // This increments frameNumber and adds an item to inputFrames list

		// When serializing and deserializing
		byte[] bytes = GameStateDerializer.serialize(gameState);
		GameState loadedGameState = GameStateDerializer.deserialize(bytes);

		// Then all serializable fields should be equal
		assertNotNull(loadedGameState);
		assertEquals(gameState.frameNumber, loadedGameState.frameNumber);

		// The uiEventChannel list serializes its size but its elements are intentionally not persisted:
		// InputEvent implementations reference live engine objects (mouse, network connections, frames).
		// World content equality assertions come with the world persistence work.
	}

	@Test
	public void testWeatherRoundTrip() {
		// Given a game state whose weather color stops are in place
		gameState.frameNumber = 123L;

		// When serializing and deserializing
		byte[] bytes = GameStateDerializer.serialize(gameState);
		GameState loadedGameState = GameStateDerializer.deserialize(bytes);

		// Then the sky color should be identical at sample points across the day-night cycle
		assertNotNull(loadedGameState);
		for (long time : new long[] {0L, 200L, 900L, 1700L, 2200L}) {
			assertEquals(gameState.weather.skyColor(time), loadedGameState.weather.skyColor(time),
					"sky color should round trip at time " + time);
		}
	}

	@Test
	public void testCloudsRoundTrip() {
		// Given a game state whose clouds have drifted away from their default position
		gameState.frameNumber = 0L;
		gameState.update(new InputEventFrame(gameState.frameNumber));
		gameState.update(new InputEventFrame(gameState.frameNumber));

		// When serializing and deserializing
		byte[] bytes = GameStateDerializer.serialize(gameState);
		GameState loadedGameState = GameStateDerializer.deserialize(bytes);

		// Then the cloud drift should round trip
		assertNotNull(loadedGameState);
		assertEquals(gameState.clouds.driftX(), loadedGameState.clouds.driftX(), 0.0001f);
		assertEquals(gameState.clouds.driftY(), loadedGameState.clouds.driftY(), 0.0001f);
	}

}
