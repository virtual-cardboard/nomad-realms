package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.LinkedList;
import nomadrealms.context.game.interaction.InteractionState;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.context.game.world.map.generation.TerrainSandboxMapInitialization;
import org.junit.jupiter.api.Test;

public class TerrainSandboxTest {

	@Test
	public void testTerrainSandboxInitialization() {
		GameState gameState = new GameState("Terrain Sandbox", new LinkedList<>(),
				new OverworldGenerationStrategy(123456789).mapInitialization(new TerrainSandboxMapInitialization()));

		assertNull(gameState.world.nomad);
		assertDoesNotThrow(() -> gameState.reindex(new LinkedList<>()));
		InteractionState is = new InteractionState(null, null, null, null);
		assertDoesNotThrow(() -> gameState.update(is));
	}

}
