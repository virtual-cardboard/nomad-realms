package nomadrealms.context.game.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.structure.RockStructure;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.generation.DefaultMapInitialization;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerMovementTest {

	private GameState gameState;
	private World world;
	private Nomad nomad;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new ArrayDeque<>(), new OverworldGenerationStrategy(12345)
				.mapInitialization(new DefaultMapInitialization()));
		world = gameState.world();
		nomad = world.nomad;
	}

	@Test
	public void testPlayerMoveInputEventQueuesMovement() {
		Tile startTile = nomad.tile();
		Tile targetTile = startTile.um(world);

		assertTrue(nomad.movementQueue().isEmpty());
		assertEquals(startTile, nomad.movementQueueEndTile());

		PlayerMoveInputEvent moveEvent = new PlayerMoveInputEvent(nomad, targetTile);
		moveEvent.resolve(world);

		assertEquals(1, nomad.movementQueue().size());
		assertEquals(targetTile, nomad.movementQueueEndTile());

		// Ensure movement did NOT go onto the card stack
		assertTrue(nomad.cardStack().getCards().isEmpty());
	}

	@Test
	public void testChainedMovements() {
		Tile tile0 = nomad.tile();
		Tile tile1 = tile0.um(world);
		Tile tile2 = tile1.ur(world);

		new PlayerMoveInputEvent(nomad, tile1).resolve(world);
		new PlayerMoveInputEvent(nomad, tile2).resolve(world);

		assertEquals(2, nomad.movementQueue().size());
		assertEquals(tile2, nomad.movementQueueEndTile());

		// Process ticks until movement completes
		InputEventFrame frame = new InputEventFrame(1);
		for (int i = 0; i < 30; i++) {
			world.update(frame);
		}

		assertEquals(tile2, nomad.tile());
		assertTrue(nomad.movementQueue().isEmpty());
	}

	@Test
	public void testObstacleCancelsMovementQueue() {
		Tile tile0 = nomad.tile();
		Tile tile1 = tile0.um(world);
		Tile tile2 = tile1.ur(world);

		new PlayerMoveInputEvent(nomad, tile1).resolve(world);
		new PlayerMoveInputEvent(nomad, tile2).resolve(world);

		// Place an obstacle on tile2
		RockStructure rock = new RockStructure();
		rock.tile(tile2);
		tile2.actor(rock);

		InputEventFrame frame = new InputEventFrame(1);
		for (int i = 0; i < 30; i++) {
			world.update(frame);
		}

		// Player moved to tile1, but tile2 was blocked, so tile2 move was cancelled
		assertEquals(tile1, nomad.tile());
		assertTrue(nomad.movementQueue().isEmpty());
	}

}
