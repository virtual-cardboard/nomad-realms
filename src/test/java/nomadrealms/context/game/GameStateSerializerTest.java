package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;

import java.util.LinkedList;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import nomadrealms.context.game.world.map.generation.TestMapGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.objenesis.instantiator.ObjectInstantiator;

public class GameStateSerializerTest {

	private GameState gameState;
	private final String filePath = "test_gamestate.sav";

	@BeforeEach
	public void setUp() {
		gameState = new GameState(new TestMapGenerationStrategy());
	}

	@Test
	public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
		GameStateSerializer serializer = new GameStateSerializer();
		serializer.kryo().register(TestMapGenerationStrategy.class);
		serializer.kryo().setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()) {
			@Override
			public ObjectInstantiator newInstantiatorOf(Class type) {
				if (type == GameState.class) {
					return () -> new GameState(new TestMapGenerationStrategy());
				}
				return super.newInstantiatorOf(type);
			}
		});

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
