package nomadrealms.game.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.world.map.generation.MapGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameMapTest {

	private GameMap gameMap;
	private World world;

	@BeforeEach
	void setUp() {
		world = new World(null, 0);
		MapGenerationStrategy strategy = new MapGenerationStrategy() {
			@Override
			public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
				Tile[][] tiles = new Tile[16][16];
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						tiles[i][j] = new Tile(chunk, new TileCoordinate(coord, i, j));
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
	void testPathFinding() {
		Tile source = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		Tile target = world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 1), 15, 15));

		List<Tile> path = gameMap.path(source, target);

		assertNotNull(path);
		assertFalse(path.isEmpty());
		assertEquals(source, path.get(0));
		assertEquals(target, path.get(path.size() - 1));
	}

}
