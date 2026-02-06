package nomadrealms.context.game.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HeavyJumpCardTest {

	private GameState gameState;
	private Nomad source;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(), new OverworldGenerationStrategy(123));
		Tile tile = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 10, 10));
		source = new Nomad("Source", tile);
		gameState.world.addActor(source);
	}

	@Test
	public void testHeavyJump_targeting() {
		Tile tileDistance1 = source.tile().um(gameState.world);
		Tile tileDistance2 = tileDistance1.um(gameState.world);
		Tile tileDistance3 = tileDistance2.um(gameState.world);

		assertTrue(GameCard.HEAVY_JUMP.targetingInfo().conditions().get(0).test(gameState.world, tileDistance2, source));
		assertFalse(GameCard.HEAVY_JUMP.targetingInfo().conditions().get(0).test(gameState.world, tileDistance1, source));
		assertFalse(GameCard.HEAVY_JUMP.targetingInfo().conditions().get(0).test(gameState.world, tileDistance3, source));
	}

	@Test
	public void testHeavyJump_effects() {
		Tile targetTile = source.tile().um(gameState.world).um(gameState.world);
		Nomad victim = new Nomad("Victim", targetTile.um(gameState.world));
		gameState.world.addActor(victim);
		victim.health(10);

		List<Effect> effects = GameCard.HEAVY_JUMP.expression().effects(gameState.world, targetTile, source);
		effects.forEach(effect -> effect.resolve(gameState.world));

		// MoveEffect should have queued an action
		assertEquals(2, source.actions().size());

		// Run the MoveAction (approx 21 ticks as calculated)
		for (int i = 0; i < 21; i++) {
			gameState.world.update(null);
		}
		assertEquals(targetTile, source.tile());

		// Run the DelayedEffectAction (this queues the damage proc chain)
		gameState.world.update(null);

		// Run the ProcChain (this applies the damage)
		gameState.world.update(null);

		// DamageEffect should have applied damage now
		assertEquals(9, victim.health());
	}
}
