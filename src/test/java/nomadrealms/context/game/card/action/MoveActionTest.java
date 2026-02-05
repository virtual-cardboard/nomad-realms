package nomadrealms.context.game.card.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoveActionTest {

	private World world;

	@BeforeEach
	void setUp() {
		GameState state = new GameState("Test World", new LinkedList<>(), new OverworldGenerationStrategy(123456789));
		this.world = state.world;
	}

	@Test
	void testNormalMove() {
		Tile sourceTile = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		sourceTile.clearActor();
		Tile nextTile = sourceTile.ur(world);
		nextTile.clearActor();
		Tile targetTile = nextTile.ur(world);
		targetTile.clearActor();

		Nomad actor = new Nomad("Actor", sourceTile);
		sourceTile.actor(actor);

		MoveAction moveAction = new MoveAction(actor, targetTile, 10);
		moveAction.update(world); // Move to nextTile

		assertEquals(nextTile, actor.tile());
		assertFalse(moveAction.isComplete());

		for (int i = 0; i < 9; i++) {
			moveAction.update(world);
		}
		assertEquals(nextTile, actor.tile());
		assertFalse(moveAction.isComplete());

		moveAction.update(world); // Move to targetTile
		assertEquals(targetTile, actor.tile());
		assertTrue(moveAction.isComplete());
	}

	@Test
	void testMoveToOccupiedTile() {
		Tile sourceTile = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		sourceTile.clearActor();
		Tile targetTile = sourceTile.ur(world);
		targetTile.clearActor();

		Nomad actor = new Nomad("Actor", sourceTile);
		sourceTile.actor(actor);

		Nomad blocker = new Nomad("Blocker", targetTile);
		targetTile.actor(blocker);

		MoveAction moveAction = new MoveAction(actor, targetTile, 10);
		moveAction.update(world);

		assertTrue(moveAction.isComplete(), "MoveAction should be complete because it's blocked");
	}

	@Test
	void testDashToOccupiedTile() {
		Tile sourceTile = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		sourceTile.clearActor();
		Tile targetTile = sourceTile.ur(world);
		targetTile.clearActor();

		Nomad actor = new Nomad("Actor", sourceTile);
		sourceTile.actor(actor);

		Nomad blocker = new Nomad("Blocker", targetTile);
		targetTile.actor(blocker);

		DashAction dashAction = new DashAction(actor, targetTile, 10);
		dashAction.update(world);

		assertTrue(dashAction.isComplete(), "DashAction should be complete because it's blocked");
	}
}
