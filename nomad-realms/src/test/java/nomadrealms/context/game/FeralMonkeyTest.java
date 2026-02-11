package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.cardplayer.FeralMonkey;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import org.junit.jupiter.api.Test;

public class FeralMonkeyTest {

	@Test
	public void testFeralMonkeyKillsNomadWithin400Ticks() {
		GameState gameState = new GameState();
		World world = gameState.world;

		Nomad target = new Nomad("Test Nomad", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 2, 3)));
		FeralMonkey feralMonkey = new FeralMonkey("Test Feral Monkey", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0)));

		world.addActor(target);
		world.addActor(feralMonkey);

		int ticks = 0;

		for (int i = 0; i < 400; i++) {
			world.update(null);
			ticks++;
			if (target.isDestroyed()) {
				break;
			}
		}
		System.out.println("Ticks taken for feral monkey to kill nomad: " + ticks);
		if (target.health() > 0) {
			System.out.println("Nomad health: " + target.health());
			System.out.println("Feral Monkey health: " + feralMonkey.health());
			System.out.println("Nomad location: " + target.tile().coord());
			System.out.println("Feral Monkey location: " + feralMonkey.tile().coord());
		}
		assertTrue(target.health() <= 0, "Feral monkey did not kill the nomad within 400 ticks");
	}

}
