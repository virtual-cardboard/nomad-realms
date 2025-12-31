package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.actor.cardplayer.Farmer;
import nomadrealms.context.game.actor.cardplayer.FeralMonkey;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import java.util.LinkedList;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TestMapGenerationStrategy;
import org.junit.jupiter.api.Test;

public class FeralMonkeyTest {

	@Test
	public void testFeralMonkeyKillsFarmerWithin400Ticks() {
		GameState gameState = new GameState(new TestMapGenerationStrategy());
		World world = gameState.world;

		Farmer farmer = new Farmer("Test Farmer", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 2, 3)));
		FeralMonkey feralMonkey = new FeralMonkey("Test Feral Monkey", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0)));

		world.actors.add(farmer);
		world.actors.add(feralMonkey);

		int ticks = 0;

		for (int i = 0; i < 400; i++) {
			world.update(null);
			ticks++;
			if (farmer.isDestroyed()) {
				break;
			}
		}
		System.out.println("Ticks taken for feral monkey to kill farmer: " + ticks);
		if (farmer.health() > 0) {
			System.out.println("Farmer health: " + farmer.health());
			System.out.println("Feral Monkey health: " + feralMonkey.health());
			System.out.println("Farmer location: " + farmer.tile().coord());
			System.out.println("Feral Monkey location: " + feralMonkey.tile().coord());
		}
		assertTrue(farmer.health() <= 0, "Feral monkey did not kill the farmer within 400 ticks");
	}

}
