package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TutorialGenerationStrategy;
import nomadrealms.context.game.world.map.generation.TutorialMapInitialization;
import nomadrealms.context.game.world.map.tile.GrassTile;
import nomadrealms.context.game.world.map.tile.VoidTile;
import org.junit.jupiter.api.Test;

public class TutorialWorldTest {

	@Test
	public void testTutorialWorldGeneration() {
		GameState gameState = new GameState("Tutorial World 1", new LinkedList<>(),
				new TutorialGenerationStrategy().mapInitialization(new TutorialMapInitialization()));

		assertNotNull(gameState.world.nomad);
		assertEquals("Donny", gameState.world.nomad.name());
		assertEquals(8, gameState.world.nomad.tile().coord().x());
		assertEquals(7, gameState.world.nomad.tile().coord().y());

		// Check player has basic movement cards
		assertTrue(gameState.world.nomad.deckCollection().deck1().size() > 0);
		assertTrue(gameState.world.nomad.deckCollection().deck2().size() > 0);

		// Check origin chunk (0,0) has exactly 10 GrassTiles and remaining 246 VoidTiles
		Chunk originChunk = gameState.world.getChunk(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0));
		assertNotNull(originChunk);

		int grassCount = 0;
		int voidCount = 0;
		for (Tile tile : originChunk.tiles()) {
			if (tile instanceof GrassTile) {
				grassCount++;
			} else if (tile instanceof VoidTile) {
				voidCount++;
			}
		}

		assertEquals(10, grassCount);
		assertEquals(246, voidCount);

		// Check neighbor chunk (1,0) contains only VoidTiles
		Chunk neighborChunk = gameState.world.getChunk(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 1, 0));
		assertNotNull(neighborChunk);

		int neighborVoidCount = 0;
		for (Tile tile : neighborChunk.tiles()) {
			if (tile instanceof VoidTile) {
				neighborVoidCount++;
			}
		}
		assertEquals(256, neighborVoidCount);

		assertDoesNotThrow(() -> gameState.update(new InputEventFrame(gameState.frameNumber)));
	}

}
