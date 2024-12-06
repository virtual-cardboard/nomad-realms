package nomadrealms.game.world.map.area.coordinate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import common.math.Vector2i;

public class TileCoordinateTest {
	ChunkCoordinate chunk = createChunk(0, 0);

	@Test
	void testDr() {
		assertTileDownRight(0, 0, 1, 8, 0, 0, 2, 9);
		assertTileDownRight(0, 0, 2, 9, 0, 0, 3, 9);  // x is even -> x increase, y stays the same
		assertTileDownRight(0, 0, 3, 9, 0, 0, 4, 10); // x is odd -> x increase, y increase
		assertTileDownRight(0, 0, 4, 10, 0, 0, 5, 10);

		assertTileDownRight(0, 0, 14, 4, 0, 0, 15, 4);
		assertTileDownRight(0, 0, 15, 4, 1, 0, 0, 5);

		assertTileDownRight(0, 0, 4, 14, 0, 0, 5, 14);
		assertTileDownRight(0, 0, 5, 14, 0, 0, 6, 15);
		assertTileDownRight(0, 0, 6, 15, 0, 0, 7, 15);
		assertTileDownRight(0, 0, 7, 15, 0, 1, 8, 0);

		assertTileDownRight(0, 0, 14, 15, 0, 0, 15, 15);
		assertTileDownRight(0, 0, 15, 15, 1, 1, 0, 0);
	}

	@Test
	void testDl() {
		assertTileDownLeft(0, 0, 1, 8, 0, 0, 0, 9);
		assertTileDownLeft(0, 0, 2, 9, 0, 0, 1, 9);  // x is even -> x decrease, y stays the same
		assertTileDownLeft(0, 0, 3, 9, 0, 0, 2, 10); // x is odd -> x decrease, y increase
		assertTileDownLeft(0, 0, 4, 10, 0, 0, 3, 10);

		assertTileDownLeft(0, 0, 14, 4, 0, 0, 13, 4);
		assertTileDownLeft(0, 0, 15, 4, 0, 0, 14, 5);

		assertTileDownLeft(0, 0, 4, 14, 0, 0, 3, 14);
		assertTileDownLeft(0, 0, 5, 14, 0, 0, 4, 15);
		assertTileDownLeft(0, 0, 6, 15, 0, 0, 5, 15);
		assertTileDownLeft(0, 0, 7, 15, 0, 1, 6, 0);

		assertTileDownLeft(0, 0, 14, 15, 0, 0, 13, 15);
		assertTileDownLeft(0, 0, 15, 15, 0, 1, 14, 0);
	}

	private void assertTileDownRight(int currChunkX, int currChunkY, int currTileX, int currTileY, int nextChunkX,
			int nextChunkY, int nextTileX, int nextTileY) {
		TileCoordinate currTile = createTile(currTileX, currTileY, currChunkX, currChunkY);
		TileCoordinate expectedDrTile = createTile(nextTileX, nextTileY, nextChunkX, nextChunkY);
		System.out.println("");
		TileCoordinate calculatedDrTile = currTile.dr();
		System.out.println("Checking down right of tile " + currTile);
		System.out.println("                   Expected  " + expectedDrTile);
		System.out.println("                 Calculated  " + calculatedDrTile);
		assertEquals(expectedDrTile.x(), calculatedDrTile.x());
		assertEquals(expectedDrTile.y(), calculatedDrTile.y());
		assertEquals(expectedDrTile.chunk().x(), calculatedDrTile.chunk().x());
		assertEquals(expectedDrTile.chunk().y(), calculatedDrTile.chunk().y());
	}

	private void assertTileDownLeft(int currChunkX, int currChunkY, int currTileX, int currTileY, int nextChunkX,
			int nextChunkY, int nextTileX, int nextTileY) {
		TileCoordinate currTile = createTile(currTileX, currTileY, currChunkX, currChunkY);
		TileCoordinate expectedDlTile = createTile(nextTileX, nextTileY, nextChunkX, nextChunkY);
		System.out.println("");
		TileCoordinate calculatedDlTile = currTile.dl();
		System.out.println("Checking down left of tile " + currTile);
		System.out.println("                   Expected  " + expectedDlTile);
		System.out.println("                 Calculated  " + calculatedDlTile);
		assertEquals(expectedDlTile.x(), calculatedDlTile.x());
		assertEquals(expectedDlTile.y(), calculatedDlTile.y());
		assertEquals(expectedDlTile.chunk().x(), calculatedDlTile.chunk().x());
		assertEquals(expectedDlTile.chunk().y(), calculatedDlTile.chunk().y());
	}

	private TileCoordinate createTile(int currTileX, int currTileY, int currChunkX, int currChunkY) {
		return new TileCoordinate(createChunk(currChunkX, currChunkY), new Vector2i(currTileX, currTileY));
	}

	private ChunkCoordinate createChunk(int chunkX, int chunkY) {
		RegionCoordinate region = new RegionCoordinate(new Vector2i(0, 0));
		ZoneCoordinate zone = new ZoneCoordinate(region, 0, 0);
		return new ChunkCoordinate(zone, chunkX, chunkY);
	}
}