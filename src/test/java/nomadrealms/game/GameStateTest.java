package nomadrealms.game;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.Queue;

import nomadrealms.game.actor.cardplayer.Farmer;
import nomadrealms.game.card.intent.DamageIntent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.ProcChain;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateTest {

	private GameState gameState;

	@BeforeEach
	public void setUp() {
		Queue<InputEvent> uiEventChannel = new LinkedList<>();
		gameState = new GameState(uiEventChannel);
	}

	@Test
	public void testQueuingProcChain() {
		DamageIntent damageIntent = new DamageIntent(null, null, 5);
		ProcChain procChain = new ProcChain(singletonList(damageIntent));
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
		gameState.world.addActor(farmer);

		boolean cardPlayed = false;
		for (int i = 0; i < 24; i++) {
			gameState.update();
			if (!farmer.lastPlays().isEmpty()) {
				cardPlayed = true;
				break;
			}
		}

		assertTrue(cardPlayed);
	}

}
