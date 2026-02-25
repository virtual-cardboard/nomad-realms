package nomadrealms.context.game.world.map.area.coordinate;

import static nomadrealms.context.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import engine.common.math.Vector2f;
import org.junit.jupiter.api.Test;

public class CoordinateConversionTest {

    @Test
    void testRegionToPixelPosition() {
        RegionCoordinate region = new RegionCoordinate(1, 2);
        Vector2f pixelPos = region.toPixelPosition();

        float expectedX = 1 * REGION_SIZE * ZONE_SIZE * CHUNK_SIZE * TILE_HORIZONTAL_SPACING;
        float expectedY = 2 * REGION_SIZE * ZONE_SIZE * CHUNK_SIZE * TILE_VERTICAL_SPACING;

        assertEquals(expectedX, pixelPos.x(), 0.01f);
        assertEquals(expectedY, pixelPos.y(), 0.01f);
    }

    @Test
    void testZoneToPixelPosition() {
        RegionCoordinate region = new RegionCoordinate(1, 2);
        ZoneCoordinate zone = new ZoneCoordinate(region, 2, 1);
        Vector2f pixelPos = zone.toPixelPosition();

        float expectedX = (1 * REGION_SIZE + 2) * ZONE_SIZE * CHUNK_SIZE * TILE_HORIZONTAL_SPACING;
        float expectedY = (2 * REGION_SIZE + 1) * ZONE_SIZE * CHUNK_SIZE * TILE_VERTICAL_SPACING;

        assertEquals(expectedX, pixelPos.x(), 0.01f);
        assertEquals(expectedY, pixelPos.y(), 0.01f);
    }

    @Test
    void testChunkToPixelPosition() {
        RegionCoordinate region = new RegionCoordinate(1, 2);
        ZoneCoordinate zone = new ZoneCoordinate(region, 2, 1);
        ChunkCoordinate chunk = new ChunkCoordinate(zone, 5, 10);
        Vector2f pixelPos = chunk.toPixelPosition();

        float expectedX = ((1 * REGION_SIZE + 2) * ZONE_SIZE + 5) * CHUNK_SIZE * TILE_HORIZONTAL_SPACING;
        float expectedY = ((2 * REGION_SIZE + 1) * ZONE_SIZE + 10) * CHUNK_SIZE * TILE_VERTICAL_SPACING;

        assertEquals(expectedX, pixelPos.x(), 0.01f);
        assertEquals(expectedY, pixelPos.y(), 0.01f);
    }

    @Test
    void testTileCoordinateOf() {
        // Test Origin
        // Tile(0,0) center is roughly (20, 17.32)
        assertTileCoordinateOf(0, 0, 0, 0, 0, 0, 0, 0, new Vector2f(20, 17.32f));

        // Test Tile(1, 1)
        // Center X: 1 * 30 + 20 = 50
        // Center Y: 1 * 34.64 + 34.64 (odd shift) = 69.28
        assertTileCoordinateOf(0, 0, 0, 0, 0, 0, 1, 1, new Vector2f(50, 69.28f));

        // Test Tile(15, 0)
        // Center X: 15 * 30 + 20 = 470
        // Center Y: 0 + 34.64 = 34.64
        assertTileCoordinateOf(0, 0, 0, 0, 0, 0, 15, 0, new Vector2f(470, 34.64f));

        // Test crossing chunk boundary
        // Chunk(1,0), Tile(0,0)
        // Center X: 16 * 30 + 20 = 500
        // Center Y: 0 + 17.32 = 17.32
        assertTileCoordinateOf(0, 0, 0, 0, 1, 0, 0, 0, new Vector2f(500, 17.32f));
    }

    private void assertTileCoordinateOf(int regionX, int regionY, int zoneX, int zoneY, int chunkX, int chunkY, int tileX, int tileY, Vector2f position) {
        TileCoordinate coord = TileCoordinate.tileCoordinateOf(position);
        assertEquals(regionX, coord.chunk().zone().region().x(), "Region X mismatch");
        assertEquals(regionY, coord.chunk().zone().region().y(), "Region Y mismatch");
        assertEquals(zoneX, coord.chunk().zone().x(), "Zone X mismatch");
        assertEquals(zoneY, coord.chunk().zone().y(), "Zone Y mismatch");
        assertEquals(chunkX, coord.chunk().x(), "Chunk X mismatch");
        assertEquals(chunkY, coord.chunk().y(), "Chunk Y mismatch");
        assertEquals(tileX, coord.x(), "Tile X mismatch");
        assertEquals(tileY, coord.y(), "Tile Y mismatch");
    }
}
