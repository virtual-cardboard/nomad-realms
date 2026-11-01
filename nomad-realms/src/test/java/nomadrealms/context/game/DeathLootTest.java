package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.cardplayer.Wolf;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.generation.DefaultMapInitialization;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeathLootTest {

	private GameState gameState;
	private Nomad nomad;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(),
				new OverworldGenerationStrategy(123456789).mapInitialization(new DefaultMapInitialization()));
		nomad = gameState.world().nomad;
		assertNotNull(nomad);
		assertNotNull(nomad.tile());
	}

	@Test
	public void testWolfDropsLootOnDeathTile() {
		Tile spawnTile = nomad.tile().ur(gameState.world());
		Wolf wolf = new Wolf("loot wolf", spawnTile);
		gameState.world().addActor(wolf, true);
		assertEquals(wolf, spawnTile.actor());

		wolf.damage(wolf.health());

		// Tick the world until the wolf is dead and its loot has landed
		for (int i = 0; i < 20 && !(wolf.dead() && !spawnTile.items().isEmpty()); i++) {
			gameState.update(null);
		}

		assertTrue(wolf.dead());
		assertFalse(spawnTile.actor() != null && spawnTile.actor() != wolf);
		assertEquals(1, spawnTile.items().size());
		WorldItem dropped = spawnTile.items().get(0);
		assertEquals(Item.GOLD_COIN, dropped.item());
	}

}
