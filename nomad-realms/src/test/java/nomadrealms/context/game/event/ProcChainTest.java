package nomadrealms.context.game.event;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import org.junit.jupiter.api.Test;

class ProcChainTest {

	@Test
	void shouldResolveImmediatelyIfDelayIsZero() {
		GameState gameState = new GameState("Test World", new LinkedList<>(), new TemplateGenerationStrategy());
		World world = gameState.world;
		TileCoordinate tileCoord = new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0);
		Nomad actor = new Nomad("Test Nomad", world.getTile(tileCoord));
		int initialHealth = actor.health();

		DamageEffect effect = new DamageEffect(actor, actor, 1);
		effect.delay(0);
		ProcChain chain = new ProcChain(singletonList(effect));

		chain.update(world);

		assertEquals(initialHealth - 1, actor.health());
		assertTrue(chain.empty());
	}

	@Test
	void shouldResolveAfterFiveTicksIfDelayIsFive() {
		GameState gameState = new GameState("Test World", new LinkedList<>(), new TemplateGenerationStrategy());
		World world = gameState.world;
		TileCoordinate tileCoord = new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0);
		Nomad actor = new Nomad("Test Nomad", world.getTile(tileCoord));
		int initialHealth = actor.health();

		DamageEffect effect = new DamageEffect(actor, actor, 1);
		effect.delay(5);
		ProcChain chain = new ProcChain(singletonList(effect));

		for (int i = 0; i < 4; i++) {
			chain.update(world);
			assertEquals(initialHealth, actor.health(), "Effect should not be resolved at tick " + (i + 1));
		}

		chain.update(world);
		assertEquals(initialHealth - 1, actor.health(), "Effect should be resolved at tick 5");
		assertTrue(chain.empty());
	}

}
