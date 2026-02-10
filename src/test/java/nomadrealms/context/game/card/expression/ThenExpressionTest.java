package nomadrealms.context.game.card.expression;

import static nomadrealms.context.game.card.expression.DamageExpression.damage;
import static nomadrealms.context.game.card.expression.DelayedExpression.delayed;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;

public class ThenExpressionTest {

	private GameState gameState;
	private Farmer source;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(),
				new OverworldGenerationStrategy(123));
		Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		source = new Farmer("Source", tile1);
		gameState.world.addActor(source);
	}

	@Test
	public void testImmediateThenImmediate() {
		source.health(10);
		CardExpression expr = damage(1).then(damage(2));
		List<Effect> effects = expr.effects(gameState.world, source, source);
		for (Effect effect : effects) {
			effect.resolve(gameState.world);
		}
		// Health should be 9 after first DamageEffect.
		assertEquals(9, source.health());

		// Damage 2 is now in a ProcChain.
		gameState.world.update(null);
		assertEquals(7, source.health());
	}

	@Test
	public void testDelayedThenImmediate() {
		source.health(10);
		// Damage 1 after 2 ticks pre-delay, 1 tick post-delay. Then Damage 2.
		CardExpression expr = delayed(damage(1), 2, 1).then(damage(2));
		List<Effect> effects = expr.effects(gameState.world, source, source);
		for (Effect effect : effects) {
			effect.resolve(gameState.world);
		}

		// Initially health is 10. No damage yet because it's delayed.
		assertEquals(10, source.health());

		// Ticks 1-2: pre-delay
		gameState.world.update(null);
		gameState.world.update(null);
		assertEquals(10, source.health());

		// Tick 3: action update called -> damage 1 added to procChain and resolved
		gameState.world.update(null);
		assertEquals(9, source.health());

		// Tick 4: post-delay 1/1
		gameState.world.update(null);
		assertEquals(9, source.health());

		// Tick 5: post-delay finished, ThenAction executes next -> damage 2 added to procChain and resolved
		gameState.world.update(null);
		assertEquals(7, source.health());
	}
}
