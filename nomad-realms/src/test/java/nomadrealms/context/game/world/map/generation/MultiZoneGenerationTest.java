package nomadrealms.context.game.world.map.generation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.LinkedList;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import org.junit.jupiter.api.Test;

public class MultiZoneGenerationTest {

	@Test
	public void testGenerationInDifferentZones() {
		GameState gameState = new GameState("Test World", new LinkedList<>(), new OverworldGenerationStrategy(123));

		// Zone (0,0)
		ZoneCoordinate zone00 = new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0);
		Tile tile00 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(zone00, 0, 0), 0, 0));
		assertNotNull(tile00);

		// Zone (1,0)
		ZoneCoordinate zone10 = new ZoneCoordinate(new RegionCoordinate(0, 0), 1, 0);
		Tile tile10 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(zone10, 0, 0), 0, 0));
		assertNotNull(tile10);

		// Zone (0,1)
		ZoneCoordinate zone01 = new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 1);
		Tile tile01 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(zone01, 0, 0), 0, 0));
		assertNotNull(tile01);
	}
}
