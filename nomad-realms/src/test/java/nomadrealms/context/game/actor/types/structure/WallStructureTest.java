package nomadrealms.context.game.actor.types.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.actor.types.cardplayer.VillageChief;
import nomadrealms.context.game.actor.types.structure.factory.StructureFactory;
import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.Test;

public class WallStructureTest {

	@Test
	public void testWallStructureTypeAndFactory() {
		WallStructure wall = new WallStructure(Math.toRadians(30));
		assertEquals(StructureType.WALL, wall.structureType());
		assertEquals(Math.toRadians(30), wall.angle(), 0.0001);

		Structure created = StructureFactory.createStructure(StructureType.WALL);
		assertNotNull(created);
		assertTrue(created instanceof WallStructure);
		assertEquals(StructureType.WALL, created.structureType());
	}

	@Test
	public void testImageForAngleAndFlipped() {
		WallStructure wall = new WallStructure(0);

		// Sector 0..60 deg (center 30, Down-Right) -> wall-1-3, unflipped
		assertEquals("wall-1-3", wall.imageForAngle(Math.toRadians(30)));
		assertFalse(wall.isFlipped(Math.toRadians(30)));

		// Sector 60..120 deg (center 90, Down-Middle) -> wall-2-4, unflipped
		assertEquals("wall-2-4", wall.imageForAngle(Math.toRadians(90)));
		assertFalse(wall.isFlipped(Math.toRadians(90)));

		// Sector 120..180 deg (center 150, Down-Left) -> wall-1-3, FLIPPED
		assertEquals("wall-1-3", wall.imageForAngle(Math.toRadians(150)));
		assertTrue(wall.isFlipped(Math.toRadians(150)));

		// Sector 180..240 deg (center 210 / -150, Up-Left) -> wall-0-2, FLIPPED
		assertEquals("wall-0-2", wall.imageForAngle(Math.toRadians(210)));
		assertEquals("wall-0-2", wall.imageForAngle(Math.toRadians(-150)));
		assertTrue(wall.isFlipped(Math.toRadians(210)));
		assertTrue(wall.isFlipped(Math.toRadians(-150)));

		// Sector 240..300 deg (center 270 / -90, Up-Middle) -> wall-1-5, unflipped
		assertEquals("wall-1-5", wall.imageForAngle(Math.toRadians(270)));
		assertEquals("wall-1-5", wall.imageForAngle(Math.toRadians(-90)));
		assertFalse(wall.isFlipped(Math.toRadians(270)));

		// Sector 300..360 deg (center 330 / -30, Up-Right) -> wall-0-2, unflipped
		assertEquals("wall-0-2", wall.imageForAngle(Math.toRadians(330)));
		assertEquals("wall-0-2", wall.imageForAngle(Math.toRadians(-30)));
		assertFalse(wall.isFlipped(Math.toRadians(330)));
	}

	@Test
	public void testVillagerSurroundedByWallsInWorld() {
		World world = new World(null, new OverworldGenerationStrategy(12345));
		ZoneCoordinate zoneCoord = new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0);
		Zone zone = world.getZone(zoneCoord);
		assertNotNull(zone);

		// Find VillageChief in the zone
		Tile chiefTile = null;
		for (int chunkX = 0; chunkX < 16; chunkX++) {
			for (int chunkY = 0; chunkY < 16; chunkY++) {
				for (int tileX = 0; tileX < 16; tileX++) {
					for (int tileY = 0; tileY < 16; tileY++) {
						ChunkCoordinate chunkCoord = new ChunkCoordinate(zoneCoord, chunkX, chunkY);
						TileCoordinate tileCoord = new TileCoordinate(chunkCoord, tileX, tileY);
						Tile tile = world.getTile(tileCoord);
						if (tile != null && tile.actor() instanceof VillageChief) {
							chiefTile = tile;
							break;
						}
					}
					if (chiefTile != null) break;
				}
				if (chiefTile != null) break;
			}
			if (chiefTile != null) break;
		}

		if (chiefTile != null) {
			Tile[] neighbors = new Tile[]{
					chiefTile.ul(world), chiefTile.um(world), chiefTile.ur(world),
					chiefTile.dl(world), chiefTile.dm(world), chiefTile.dr(world)
			};
			int wallCount = 0;
			for (Tile neighbor : neighbors) {
				if (neighbor != null && neighbor.actor() instanceof WallStructure) {
					wallCount++;
				}
			}
			assertTrue(wallCount > 0, "Expected neighbor tiles to contain WallStructures");
		}
	}

}
