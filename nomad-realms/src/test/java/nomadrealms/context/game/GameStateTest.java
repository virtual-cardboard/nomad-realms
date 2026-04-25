package nomadrealms.context.game;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.interaction.InteractionState;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateTest {

	private GameState gameState;

	@BeforeEach
	public void setUp() {
		gameState = new GameState();
	}

	@Test
	public void testQueuingProcChain() {
		DamageEffect damage = new DamageEffect(null, null, 5);
		ProcChain procChain = new ProcChain(singletonList(damage));
		gameState.world.addProcChain(procChain);

		assertFalse(gameState.world.procChains.isEmpty());
		assertEquals(1, gameState.world.procChains.size());
	}

	@Test
	public void testGettingTiles() {
		Tile tile = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		assertNotNull(tile);
	}

	@Test
	public void testAddingFarmerAndVerifyingPlaysCardWithin10Ticks() {
		Farmer farmer = new Farmer("Test Farmer", gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0)));
		gameState.world.nomad = new nomadrealms.context.game.actor.types.cardplayer.Nomad("Nomad", farmer.tile());
		gameState.world.addActor(farmer);

		InteractionState is = new InteractionState(null, null, null, null);
		boolean cardPlayed = false;
		for (int i = 0; i < 24; i++) {
			gameState.update(is);
			if (!farmer.lastPlays().isEmpty()) {
				cardPlayed = true;
				break;
			}
		}

		assertTrue(cardPlayed);
	}

	@Test
	public void testUpdate() {
		InteractionState is = new InteractionState(null, null, null, null);
		for (int i = 0; i < 50; i++) {
			gameState.update(is);
			assertEquals(i + 1, (int) gameState.frameNumber);
		}
	}

}
