package nomadrealms.context.game.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationParameters;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.GenerationLayer;
import org.junit.jupiter.api.Test;

public class GenerationLayerTest {

	static class MockGenerationStrategy extends MapGenerationStrategy {
		@Override public MapGenerationParameters parameters() { return new MapGenerationParameters().seed(0); }
		@Override public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) { return new Tile[0][0]; }
		@Override public void generateBiome(Zone zone, Zone[][] surrounding) {}
		@Override public void generatePoints(Zone zone, Zone[][] surrounding) {}
		@Override public Chunk[][] generateTiles(Zone zone) { return new Chunk[0][0]; }
		@Override public void generateStructure(Zone zone, Zone[][] surrounding) {}
		@Override public void generateVillager(Zone zone, Zone[][] surrounding) {}
		@Override public Chunk[][] generateZone(World world, Zone zone) { return new Chunk[0][0]; }
	}

	@Test
	public void testLayerGeneration() {
		World world = new World(null, new MockGenerationStrategy());
		ZoneCoordinate coord = new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0);
		Zone zone = world.getZone(coord);
		assertEquals(GenerationLayer.FINISHED, zone.currentLayer());
	}

	@Test
	public void testLayerRadii() {
		World world = new World(null, new MockGenerationStrategy());
		ZoneCoordinate center = new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0);
		Zone zone = world.getRegion(center.region()).lazyGetZone(center);

		world.ensureGenerated(zone, GenerationLayer.FINISHED);

		// Villager (Radius 1)
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 && y == 0) continue;
				ZoneCoordinate neighbor = new ZoneCoordinate(center.region(), center.x() + x, center.y() + y).normalize();
				assertAtLeast(GenerationLayer.VILLAGER, world.getRegion(neighbor.region()).lazyGetZone(neighbor).currentLayer(), "Radius 1 failed for VILLAGER at " + neighbor);
			}
		}

		// Structure (Radius 2)
		for (int x = -2; x <= 2; x++) {
			for (int y = -2; y <= 2; y++) {
				if (Math.max(Math.abs(x), Math.abs(y)) < 2) continue;
				ZoneCoordinate neighbor = new ZoneCoordinate(center.region(), center.x() + x, center.y() + y).normalize();
				assertAtLeast(GenerationLayer.STRUCTURE, world.getRegion(neighbor.region()).lazyGetZone(neighbor).currentLayer(), "Radius 2 failed for STRUCTURE at " + neighbor);
			}
		}

		// Points (Radius 3)
		for (int x = -3; x <= 3; x++) {
			for (int y = -3; y <= 3; y++) {
				if (Math.max(Math.abs(x), Math.abs(y)) < 3) continue;
				ZoneCoordinate neighbor = new ZoneCoordinate(center.region(), center.x() + x, center.y() + y).normalize();
				assertAtLeast(GenerationLayer.POINTS, world.getRegion(neighbor.region()).lazyGetZone(neighbor).currentLayer(), "Radius 3 failed for POINTS at " + neighbor);
			}
		}

		// Biome (Radius 4)
		for (int x = -4; x <= 4; x++) {
			for (int y = -4; y <= 4; y++) {
				if (Math.max(Math.abs(x), Math.abs(y)) < 4) continue;
				ZoneCoordinate neighbor = new ZoneCoordinate(center.region(), center.x() + x, center.y() + y).normalize();
				assertAtLeast(GenerationLayer.BIOME, world.getRegion(neighbor.region()).lazyGetZone(neighbor).currentLayer(), "Radius 4 failed for BIOME at " + neighbor);
			}
		}
	}

	private void assertAtLeast(GenerationLayer expected, GenerationLayer actual, String message) {
		if (actual.ordinal() < expected.ordinal()) {
			throw new AssertionError(message + " ==> expected at least: <" + expected + "> but was: <" + actual + ">");
		}
	}
}
