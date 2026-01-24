package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.actor.types.cardplayer.Wolf;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import org.junit.jupiter.api.Test;

public class WolfTest {

	@Test
	public void testWolfKillsFarmerWithin400Ticks() {
		GameState gameState = new GameState();
		World world = gameState.world;

		Farmer farmer = new Farmer("Test Farmer", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 2, 3)));
		Wolf wolf = new Wolf("Test Wolf", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0)));

		world.actors.add(farmer);
		world.actors.add(wolf);

		int ticks = 0;

		for (int i = 0; i < 400; i++) {
			world.update(null);
			ticks++;
			if (farmer.isDestroyed()) {
				break;
			}
		}
		System.out.println("Ticks taken for wolf to kill farmer: " + ticks);
		if (farmer.health() > 0) {
			System.out.println("Farmer health: " + farmer.health());
			System.out.println("Wolf health: " + wolf.health());
			System.out.println("Farmer location: " + farmer.tile().coord());
			System.out.println("Wolf location: " + wolf.tile().coord());
		}
		assertTrue(farmer.health() <= 0, "Wolf did not kill the farmer within 400 ticks");
	}

}
