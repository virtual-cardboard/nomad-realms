package nomadrealms.context.game.actor.types.structure;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.WALL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.actor.types.cardplayer.VillageChief;
import nomadrealms.context.game.actor.types.structure.factory.StructureFactory;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationLayer;

import org.junit.jupiter.api.Test;

public class WallStructureTest {

	@Test
	public void testWallStructureTypeAndFactory() {
		WallStructure wall = new WallStructure();
		assertEquals(WALL, wall.structureType());
		assertEquals("wall", wall.name());

		Structure factoryWall = StructureFactory.createStructure(WALL);
		assertTrue(factoryWall instanceof WallStructure);
		assertEquals(WALL, factoryWall.structureType());
	}

	@Test
	public void testImageForAngle() {
		// 30° -> Down-Right -> wall-1-3
		assertEquals("wall-1-3", WallStructure.imageForAngle(Math.toRadians(30)));
		// 90° -> Down-Middle -> wall-2-4
		assertEquals("wall-2-4", WallStructure.imageForAngle(Math.toRadians(90)));
		// 150° -> Down-Left -> wall-0-3
		assertEquals("wall-0-3", WallStructure.imageForAngle(Math.toRadians(150)));
		// 210° -> Up-Left -> wall-1-4
		assertEquals("wall-1-4", WallStructure.imageForAngle(Math.toRadians(210)));
		// 270° -> Up-Middle -> wall-1-5
		assertEquals("wall-1-5", WallStructure.imageForAngle(Math.toRadians(270)));
		// 330° -> Up-Right -> wall-0-2
		assertEquals("wall-0-2", WallStructure.imageForAngle(Math.toRadians(330)));

		// Test negative angle (-90° = 270°)
		assertEquals("wall-1-5", WallStructure.imageForAngle(Math.toRadians(-90)));
		// Test angle > 360° (390° = 30°)
		assertEquals("wall-1-3", WallStructure.imageForAngle(Math.toRadians(390)));
	}

	@Test
	public void testWallConstructorWithAngle() {
		WallStructure wall30 = new WallStructure(Math.toRadians(30));
		WallStructure wall270 = new WallStructure(Math.toRadians(270));

		assertNotNull(wall30);
		assertNotNull(wall270);
	}

	@Test
	public void testVillagerSurroundedByWalls() {
		long seed = 123456789;
		World world = new World(null, new OverworldGenerationStrategy(seed));

		// Search loaded zones for a VillageChief actor
		VillageChief villageChief = null;
		Tile chiefTile = null;

		for (int zx = 0; zx < 3; zx++) {
			for (int zy = 0; zy < 3; zy++) {
				Zone zone = world.getZone(new ZoneCoordinate(new RegionCoordinate(0, 0), zx, zy), GenerationLayer.VILLAGER);
				ChunkCoordinate[][] chunkCoords = zone.coord().chunkCoordinates();
				for (int cx = 0; cx < chunkCoords.length; cx++) {
					for (int cy = 0; cy < chunkCoords[cx].length; cy++) {
						ChunkCoordinate chunkCoord = chunkCoords[cx][cy];
						for (int tx = 0; tx < ChunkCoordinate.CHUNK_SIZE; tx++) {
							for (int ty = 0; ty < ChunkCoordinate.CHUNK_SIZE; ty++) {
								Tile tile = zone.getTile(new TileCoordinate(chunkCoord, tx, ty));
								if (tile.actor() instanceof VillageChief) {
									villageChief = (VillageChief) tile.actor();
									chiefTile = tile;
									break;
								}
							}
						}
					}
				}
			}
		}

		assertNotNull(villageChief, "VillageChief should be generated in world");
		assertNotNull(chiefTile, "Chief tile should be found");

		Tile[] neighbors = {
				chiefTile.ul(world), chiefTile.um(world), chiefTile.ur(world),
				chiefTile.dl(world), chiefTile.dm(world), chiefTile.dr(world)
		};

		int wallCount = 0;
		for (Tile neighbor : neighbors) {
			if (neighbor != null && neighbor.actor() instanceof WallStructure) {
				wallCount++;
			}
		}

		assertEquals(6, wallCount, "VillageChief should be surrounded by 6 wall structures");
	}

}
