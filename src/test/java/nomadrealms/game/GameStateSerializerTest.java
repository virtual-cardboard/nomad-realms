package nomadrealms.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameStateSerializerTest {

    private GameState gameState;
    private final String filePath = "test_gamestate.sav";

    @BeforeEach
    public void setUp() {
        Queue<InputEvent> uiEventChannel = new LinkedList<>();
        gameState = new GameState(uiEventChannel);
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        // Serialize the game state
        GameStateSerializer.serialize(gameState, filePath);

        // Deserialize the game state
        GameState loadedGameState = GameStateSerializer.deserialize(filePath);

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
