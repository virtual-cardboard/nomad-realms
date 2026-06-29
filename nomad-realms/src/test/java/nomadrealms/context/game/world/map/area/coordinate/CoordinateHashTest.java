package nomadrealms.context.game.world.map.area.coordinate;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class CoordinateHashTest {

	@Test
	void testTileCoordinateHashCodeUniqueness() {
		RegionCoordinate region = new RegionCoordinate(0, 0);
		ZoneCoordinate zone = new ZoneCoordinate(region, 0, 0);
		ChunkCoordinate chunk1 = new ChunkCoordinate(zone, 0, 0);
		ChunkCoordinate chunk2 = new ChunkCoordinate(zone, 1, 1);

		TileCoordinate tile1 = new TileCoordinate(chunk1, 5, 5);
		TileCoordinate tile2 = new TileCoordinate(chunk2, 5, 5);

		// Currently, these might have the same hashCode because TileCoordinate doesn't override hashCode
		// and only uses x,y from the base Coordinate class.
		assertNotEquals(tile1.hashCode(), tile2.hashCode(), "Tiles in different chunks should have different hash codes");
	}

	@Test
	void testChunkCoordinateHashCodeUniqueness() {
		RegionCoordinate region = new RegionCoordinate(0, 0);
		ZoneCoordinate zone1 = new ZoneCoordinate(region, 0, 0);
		ZoneCoordinate zone2 = new ZoneCoordinate(region, 1, 1);

		ChunkCoordinate chunk1 = new ChunkCoordinate(zone1, 5, 5);
		ChunkCoordinate chunk2 = new ChunkCoordinate(zone2, 5, 5);

		assertNotEquals(chunk1.hashCode(), chunk2.hashCode(), "Chunks in different zones should have different hash codes");
	}

	@Test
	void testZoneCoordinateHashCodeUniqueness() {
		RegionCoordinate region1 = new RegionCoordinate(0, 0);
		RegionCoordinate region2 = new RegionCoordinate(1, 1);

		ZoneCoordinate zone1 = new ZoneCoordinate(region1, 5, 5);
		ZoneCoordinate zone2 = new ZoneCoordinate(region2, 5, 5);

		assertNotEquals(zone1.hashCode(), zone2.hashCode(), "Zones in different regions should have different hash codes");
	}
}
