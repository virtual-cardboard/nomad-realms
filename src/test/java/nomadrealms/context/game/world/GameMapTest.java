package nomadrealms.context.game.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.MainWorldGenerationStrategy;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.tile.GrassTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameMapTest {

	private GameMap gameMap;
	private World world;

	@BeforeEach
	void setUp() {
		world = new World(null, new MainWorldGenerationStrategy(123456789));
		MapGenerationStrategy strategy = new MapGenerationStrategy() {
			@Override
			public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
				Tile[][] tiles = new Tile[16][16];
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						tiles[i][j] = new GrassTile(chunk, new TileCoordinate(coord, i, j));
					}
				}
				return tiles;
			}

			@Override
			public Chunk[][] generateZone(World world, Zone zone) {
				Chunk[][] chunks = new Chunk[16][16];
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						Chunk chunk = new Chunk(zone, new ChunkCoordinate(zone.coord(), i, j));
						chunk.tiles(generateChunk(zone, chunk, new ChunkCoordinate(zone.coord(), i, j)));
						chunks[i][j] = chunk;
					}
				}
				return chunks;
			}
		};
		gameMap = new GameMap(world, strategy);
	}

	@Test
	void testSimplePathFinding() {
		Tile source = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 1), 13, 0));
		Tile target = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 15, 15));

		List<Tile> path = gameMap.path(source, target);

		assertNotNull(path);
		assertEquals(3, path.size());
		assertEquals(source, path.get(0));
		assertEquals(source.ur(world), path.get(1));
		assertEquals(target, path.get(path.size() - 1));
	}

	@Test
	void testSeedConsistency() {
		long seed = 123456789;
		World world1 = new World(null, new MainWorldGenerationStrategy(seed));
		World world2 = new World(null, new MainWorldGenerationStrategy(seed));

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				Tile tile1 = world1.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), i, j));
				Tile tile2 = world2.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), i, j));
				assertEquals(tile1.coord().x(), tile2.coord().x());
				assertEquals(tile1.coord().y(), tile2.coord().y());
			}
		}
	}
}
