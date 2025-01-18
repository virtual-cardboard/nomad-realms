package nomadrealms.game.world.map.area.coordinate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import common.math.Vector2i;

public class TileCoordinateTest {

	ChunkCoordinate chunk = createChunk(0, 0);

	@Test
	void testDr() {
		assertTileDownRight(0, 0, 1, 8, 0, 0, 2, 9);
		assertTileDownRight(0, 0, 2, 9, 0, 0, 3, 9); // x is even -> x increase, y stays the same
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
		assertTileDownLeft(0, 0, 2, 9, 0, 0, 1, 9); // x is even -> x decrease, y stays the same
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
		TileCoordinate calculatedDrTile = currTile.dr();
		assertEquals(expectedDrTile.x(), calculatedDrTile.x());
		assertEquals(expectedDrTile.y(), calculatedDrTile.y());
		assertEquals(expectedDrTile.chunk().x(), calculatedDrTile.chunk().x());
		assertEquals(expectedDrTile.chunk().y(), calculatedDrTile.chunk().y());
	}

	private void assertTileDownLeft(int currChunkX, int currChunkY, int currTileX, int currTileY, int nextChunkX,
			int nextChunkY, int nextTileX, int nextTileY) {
		TileCoordinate currTile = createTile(currTileX, currTileY, currChunkX, currChunkY);
		TileCoordinate expectedDlTile = createTile(nextTileX, nextTileY, nextChunkX, nextChunkY);
		TileCoordinate calculatedDlTile = currTile.dl();
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

	@Test
	void testTileCoordinateDistance() {
		{
			TileCoordinate tile1 = new TileCoordinate(chunk, 0, 0);
			TileCoordinate tile2 = new TileCoordinate(chunk, 0, 1);
			TileCoordinate tile3 = new TileCoordinate(chunk, 1, 0);
			TileCoordinate tile4 = new TileCoordinate(chunk, 1, 1);
			assertEquals(0, tile1.distanceTo(tile1));
			assertEquals(1, tile1.distanceTo(tile2));
			assertEquals(1, tile1.distanceTo(tile3));
			assertEquals(2, tile1.distanceTo(tile4));
			assertEquals(1, tile2.distanceTo(tile4));
			assertEquals(1, tile3.distanceTo(tile4));
			assertEquals(1, tile2.distanceTo(tile3));

			assertEquals(0, tile1.distanceTo(tile1));
			assertEquals(1, tile2.distanceTo(tile1));
			assertEquals(1, tile3.distanceTo(tile1));
			assertEquals(2, tile4.distanceTo(tile1));
			assertEquals(1, tile4.distanceTo(tile2));
			assertEquals(1, tile4.distanceTo(tile3));
			assertEquals(1, tile3.distanceTo(tile2));
		}

		{
			ChunkCoordinate chunk00 = createChunk(0, 0);
			ChunkCoordinate chunk10 = createChunk(1, 0);
			ChunkCoordinate farChunk = new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(-1, 0), 2, 0), 15,
					0);
			TileCoordinate tile1 = new TileCoordinate(chunk00, 0, 2);
			TileCoordinate tile2 = new TileCoordinate(chunk10, 4, 0);
			TileCoordinate tile3 = new TileCoordinate(farChunk, 15, 2);

			assertEquals(20, tile1.distanceTo(tile2));
			assertEquals(20, tile2.distanceTo(tile1));
			assertEquals(1, tile1.distanceTo(tile3));
			assertEquals(1, tile3.distanceTo(tile1));
			assertEquals(21, tile2.distanceTo(tile3));
			assertEquals(21, tile3.distanceTo(tile2));
		}
	}

}
