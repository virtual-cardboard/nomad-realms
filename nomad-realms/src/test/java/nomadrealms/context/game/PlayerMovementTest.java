package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.structure.TreeStructure;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.event.PlayerMoveInputEvent;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.generation.DefaultMapInitialization;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerMovementTest {

	private GameState gameState;
	private Nomad nomad;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(),
				new OverworldGenerationStrategy(123456789).mapInitialization(new DefaultMapInitialization()));
		nomad = gameState.world().nomad;
		assertNotNull(nomad);
		assertNotNull(nomad.tile());
	}

	@Test
	public void testMovementQueueAndGhostCoordinate() {
		Tile startTile = nomad.tile();
		Tile neighborTile1 = startTile.ul(gameState.world());
		assertNotNull(neighborTile1);

		assertEquals(startTile.coord(), nomad.getGhostTileCoordinate());

		// Dispatch PlayerMoveInputEvent
		PlayerMoveInputEvent event1 = new PlayerMoveInputEvent(nomad, neighborTile1.coord());
		InputEventFrame frame = new InputEventFrame(gameState.frameNumber);
		frame.addEvent(event1);

		gameState.update(frame);

		// Ghost tile coordinate should now equal neighborTile1's coordinate
		assertEquals(neighborTile1.coord(), nomad.getGhostTileCoordinate());

		// CardStack should remain empty (movement queue is separate)
		assertTrue(nomad.cardStack().getCards().isEmpty());

		// Process ticks until Nomad reaches neighborTile1
		for (int i = 0; i < 50 && !nomad.tile().coord().equals(neighborTile1.coord()); i++) {
			gameState.update(null);
		}

		assertEquals(neighborTile1.coord(), nomad.tile().coord());
		assertEquals(neighborTile1.coord(), nomad.getGhostTileCoordinate());
	}

	@Test
	public void testMultipleMovementQueueing() {
		Tile startTile = nomad.tile();
		Tile neighbor1 = startTile.ur(gameState.world());
		Tile neighbor2 = neighbor1.ur(gameState.world());
		assertNotNull(neighbor1);
		assertNotNull(neighbor2);

		// Queue step 1
		PlayerMoveInputEvent event1 = new PlayerMoveInputEvent(nomad, neighbor1.coord());
		InputEventFrame frame1 = new InputEventFrame(gameState.frameNumber);
		frame1.addEvent(event1);
		gameState.update(frame1);

		// Queue step 2
		PlayerMoveInputEvent event2 = new PlayerMoveInputEvent(nomad, neighbor2.coord());
		InputEventFrame frame2 = new InputEventFrame(gameState.frameNumber);
		frame2.addEvent(event2);
		gameState.update(frame2);

		// Ghost coordinate should be neighbor2
		assertEquals(neighbor2.coord(), nomad.getGhostTileCoordinate());

		// Process ticks until neighbor1 is reached
		for (int i = 0; i < 50 && !nomad.tile().coord().equals(neighbor1.coord()); i++) {
			gameState.update(null);
		}
		assertEquals(neighbor1.coord(), nomad.tile().coord());

		// Process ticks until neighbor2 is reached
		for (int i = 0; i < 50 && !nomad.tile().coord().equals(neighbor2.coord()); i++) {
			gameState.update(null);
		}

		assertEquals(neighbor2.coord(), nomad.tile().coord());
	}

	@Test
	public void testObstacleCancelsMovementQueue() {
		Tile startTile = nomad.tile();
		Tile neighbor1 = startTile.dl(gameState.world());
		Tile neighbor2 = neighbor1.dl(gameState.world());

		// Place an obstacle at neighbor1 before movement executes
		TreeStructure tree = new TreeStructure();
		tree.tile(neighbor1);
		gameState.world().addActor(tree);

		// Queue step 1 and step 2
		PlayerMoveInputEvent event1 = new PlayerMoveInputEvent(nomad, neighbor1.coord());
		PlayerMoveInputEvent event2 = new PlayerMoveInputEvent(nomad, neighbor2.coord());
		InputEventFrame frame = new InputEventFrame(gameState.frameNumber);
		frame.addEvent(event1);
		frame.addEvent(event2);
		gameState.update(frame);

		// Process ticks
		for (int i = 0; i < 30; i++) {
			gameState.update(null);
		}

		// Movement should be cancelled because neighbor1 was obstructed
		assertEquals(startTile.coord(), nomad.tile().coord());
		assertTrue(nomad.movementQueue().isEmpty());
	}

}
