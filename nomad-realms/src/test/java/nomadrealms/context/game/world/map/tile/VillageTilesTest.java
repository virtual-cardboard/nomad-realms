package nomadrealms.context.game.world.map.tile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.context.game.world.map.tile.factory.TileFactory;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import org.junit.jupiter.api.Test;

public class VillageTilesTest {

	@Test
	public void testTileTypesAndFactory() {
		ZoneCoordinate zoneCoord = new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0);
		Chunk chunk = new Chunk(null, new ChunkCoordinate(zoneCoord, 0, 0));
		TileCoordinate coord = new TileCoordinate(chunk.coord(), 0, 0);

		Tile cobblestone = TileFactory.createTile(TileType.COBBLESTONE, chunk, coord);
		assertTrue(cobblestone instanceof CobblestoneTile);
		assertEquals(TileType.COBBLESTONE, cobblestone.type());

		Tile woodFloor = TileFactory.createTile(TileType.WOOD_FLOOR, chunk, coord);
		assertTrue(woodFloor instanceof WoodFloorTile);
		assertEquals(TileType.WOOD_FLOOR, woodFloor.type());
	}

	@Test
	public void testVillagerGenerationStepCreatesWoodFloor() {
		World world = new World(null, new OverworldGenerationStrategy(12345L));
		ZoneCoordinate zoneCoord = new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0);
		Zone zone = world.getZone(zoneCoord);
		assertNotNull(zone);

		boolean hasWoodFloor = false;
		for (int chunkX = 0; chunkX < 16; chunkX++) {
			for (int chunkY = 0; chunkY < 16; chunkY++) {
				for (int tileX = 0; tileX < 16; tileX++) {
					for (int tileY = 0; tileY < 16; tileY++) {
						ChunkCoordinate chunkCoord = new ChunkCoordinate(zoneCoord, chunkX, chunkY);
						TileCoordinate tileCoord = new TileCoordinate(chunkCoord, tileX, tileY);
						Tile tile = world.getTile(tileCoord);
						if (tile instanceof WoodFloorTile) {
							hasWoodFloor = true;
							break;
						}
					}
					if (hasWoodFloor) break;
				}
				if (hasWoodFloor) break;
			}
			if (hasWoodFloor) break;
		}
		assertTrue(hasWoodFloor, "Village should contain WoodFloorTile");
	}

}
