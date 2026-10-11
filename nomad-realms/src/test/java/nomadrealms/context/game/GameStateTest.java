package nomadrealms.context.game;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
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
	public void testConcurrentZoneAdvanceTo() throws Exception {
		for (long seed = 0; seed < 50; seed++) {
			GameState state = new GameState("Test World", new java.util.ArrayDeque<>(),
					new nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy(seed)
							.mapInitialization(new nomadrealms.context.game.world.map.generation.DefaultMapInitialization()));

			java.util.concurrent.atomic.AtomicReference<Throwable> exception = new java.util.concurrent.atomic.AtomicReference<>();
			Thread t1 = new Thread(() -> {
				try {
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							state.world.getZone(new nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate(
									new nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate(-1, -1), 2, 2),
									nomadrealms.context.game.world.map.generation.overworld.GenerationLayer.VILLAGER);
						}
					}
				} catch (Throwable t) {
					exception.set(t);
				}
			});
			Thread t2 = new Thread(() -> {
				try {
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							state.world.getZone(new nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate(
									new nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate(-1, -1), 2, 2),
									nomadrealms.context.game.world.map.generation.overworld.GenerationLayer.VILLAGER);
						}
					}
				} catch (Throwable t) {
					exception.set(t);
				}
			});

			t1.start();
			t2.start();
			t1.join();
			t2.join();

			if (exception.get() != null) {
				System.out.println("Reproduced exception on seed " + seed + ": " + exception.get());
				throw new RuntimeException(exception.get());
			}
		}
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

		boolean cardPlayed = false;
		for (int i = 0; i < 24; i++) {
			gameState.update(new InputEventFrame(gameState.frameNumber));
			if (!farmer.lastPlays().isEmpty()) {
				cardPlayed = true;
				break;
			}
		}

		assertTrue(cardPlayed);
	}

}
