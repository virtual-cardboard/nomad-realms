package nomadrealms.context.game.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.context.game.world.map.tile.GrassTile;
import nomadrealms.context.game.world.map.tile.VoidTile;
import nomadrealms.context.game.world.map.tile.WaterTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameMapTest {

	private GameMap map;
	private World world;

	@BeforeEach
	void setUp() {
		GameState state = new GameState("Test World", new LinkedList<>(), new OverworldGenerationStrategy(123456789));
		this.world = state.world;
		this.map = world.map();
	}

	@Test
	void testSimplePathFinding() {
		Tile source = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 1), 13, 0));
		Tile target = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 15, 15));

		List<Tile> path = map.path(source, target);

		assertNotNull(path);
		assertEquals(3, path.size());
		assertEquals(source, path.get(0));
		assertEquals(source.ur(world), path.get(1));
		assertEquals(target, path.get(path.size() - 1));
	}

	@Test
	void testSeedConsistency() {
		long seed = 123456789;
		World world1 = new World(null, new OverworldGenerationStrategy(seed));
		World world2 = new World(null, new OverworldGenerationStrategy(seed));

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				Tile tile1 = world1.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), i, j));
				Tile tile2 = world2.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), i, j));
				assertEquals(tile1.coord().x(), tile2.coord().x());
				assertEquals(tile1.coord().y(), tile2.coord().y());
			}
		}
	}

	@Test
	void testTileWalkability() {
		TileCoordinate dummyCoord = new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0);
		Tile grassTile = new GrassTile(null, dummyCoord);
		Tile waterTile = new WaterTile(null, dummyCoord);
		Tile voidTile = new VoidTile(null, dummyCoord);

		assertTrue(grassTile.walkable());
		assertTrue(waterTile.walkable());
		assertFalse(voidTile.walkable());
	}

	@Test
	void testPathfindingBlockedByVoidTile() {
		Tile source = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 1), 13, 0));
		Tile target = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 15, 15));

		// Replace target with VoidTile
		VoidTile voidTarget = new VoidTile(target.chunk(), target.coord());
		world.setTile(voidTarget);

		List<Tile> path = map.path(source, voidTarget);
		assertTrue(path.isEmpty());
	}

	@Test
	void testPathfindingReroutesAroundVoidTile() {
		Tile source = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 1), 13, 0));
		Tile target = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 15, 15));

		Tile directIntermediate = source.ur(world);
		VoidTile voidIntermediate = new VoidTile(directIntermediate.chunk(), directIntermediate.coord());
		world.setTile(voidIntermediate);

		List<Tile> path = map.path(source, target);
		assertNotNull(path);
		assertFalse(path.isEmpty());
		assertEquals(source, path.get(0));
		assertEquals(target, path.get(path.size() - 1));
		for (Tile tile : path) {
			assertTrue(tile.walkable());
			assertFalse(tile instanceof VoidTile);
		}
	}
}
