package nomadrealms.context.game;

import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.actor.types.cardplayer.FeralMonkey;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationParameters;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.tile.GrassTile;
import org.junit.jupiter.api.Test;

public class FeralMonkeyTest {

	@Test
	public void testFeralMonkeyKillsFarmerWithin400Ticks() {
		GameState gameState = new GameState("Test World", new LinkedList<>(), new SimpleMapGenerationStrategy());
		World world = gameState.world;

		Farmer farmer = new Farmer("Test Farmer", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 2, 3)));
		farmer.setAi(null);
		FeralMonkey feralMonkey = new FeralMonkey("Test Feral Monkey", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0)));

		world.addActor(farmer);
		world.addActor(feralMonkey);

		int ticks = 0;

		for (int i = 0; i < 400; i++) {
			world.update(null);
			ticks++;
			if (farmer.isDestroyed()) {
				break;
			}
		}
		System.out.println("Ticks taken for feral monkey to kill farmer: " + ticks);
		if (farmer.health() > 0) {
			System.out.println("Farmer health: " + farmer.health());
			System.out.println("Feral Monkey health: " + feralMonkey.health());
			System.out.println("Farmer location: " + farmer.tile().coord());
			System.out.println("Feral Monkey location: " + feralMonkey.tile().coord());
		}
		assertTrue(farmer.health() <= 0, "Feral monkey did not kill the farmer within 400 ticks");
	}

	private static class SimpleMapGenerationStrategy extends MapGenerationStrategy {
		@Override
		public MapGenerationParameters parameters() {
			return new MapGenerationParameters();
		}

		@Override
		public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
			Tile[][] tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
			for (int x = 0; x < CHUNK_SIZE; x++) {
				for (int y = 0; y < CHUNK_SIZE; y++) {
					tiles[x][y] = new GrassTile(chunk, new TileCoordinate(chunk.coord(), x, y));
				}
			}
			return tiles;
		}

		@Override
		public Chunk[][] generateZone(World world, Zone zone) {
			Chunk[][] chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
			for (int x = 0; x < ZONE_SIZE; x++) {
				for (int y = 0; y < ZONE_SIZE; y++) {
					ChunkCoordinate cc = new ChunkCoordinate(zone.coord(), x, y);
					Chunk chunk = new Chunk(zone, cc);
					chunk.tiles(generateChunk(zone, chunk, cc));
					chunks[x][y] = chunk;
				}
			}
			return chunks;
		}
	}

}
