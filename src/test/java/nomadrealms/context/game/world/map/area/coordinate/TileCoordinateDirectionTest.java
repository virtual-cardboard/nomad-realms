package nomadrealms.context.game.world.map.area.coordinate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import engine.common.math.Vector2i;
import org.junit.jupiter.api.Test;

public class TileCoordinateDirectionTest {

	private ChunkCoordinate chunk = createChunk(0, 0);

	private ChunkCoordinate createChunk(int chunkX, int chunkY) {
		RegionCoordinate region = new RegionCoordinate(new Vector2i(0, 0));
		ZoneCoordinate zone = new ZoneCoordinate(region, 0, 0);
		return new ChunkCoordinate(zone, chunkX, chunkY);
	}

	private TileCoordinate tile(int x, int y) {
		return new TileCoordinate(chunk, x, y);
	}

	@Test
	void testInverses() {
		// Test across a range of coordinates to cover even/odd columns and chunk boundaries
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				TileCoordinate t = tile(x, y);

				// um (Up) <-> dm (Down)
				assertEquals(t, t.um().dm(), "um -> dm should return to original at " + t);
				assertEquals(t, t.dm().um(), "dm -> um should return to original at " + t);

				// ul (Up Left) <-> dr (Down Right)
				assertEquals(t, t.ul().dr(), "ul -> dr should return to original at " + t);
				assertEquals(t, t.dr().ul(), "dr -> ul should return to original at " + t);

				// ur (Up Right) <-> dl (Down Left)
				assertEquals(t, t.ur().dl(), "ur -> dl should return to original at " + t);
				assertEquals(t, t.dl().ur(), "dl -> ur should return to original at " + t);
			}
		}
	}
}
