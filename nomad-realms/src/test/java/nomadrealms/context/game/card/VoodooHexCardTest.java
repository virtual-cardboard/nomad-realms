package nomadrealms.context.game.card;

import static nomadrealms.context.game.card.GameCard.VOODOO_HEX;
import static nomadrealms.context.game.card.GameCard.FEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VoodooHexCardTest {

	private GameState gameState;
	private Farmer source;
	private Farmer nearTarget;
	private Farmer farTarget;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(),
				new TemplateGenerationStrategy());

		Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		Tile tile2 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 1));
		Tile tile3 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 5));

		source = new Farmer("Source", tile1);
		nearTarget = new Farmer("NearTarget", tile2);
		farTarget = new Farmer("FarTarget", tile3);

		gameState.world.addActor(source, true);
		gameState.world.addActor(nearTarget, true);
		gameState.world.addActor(farTarget, true);
	}

	@Test
	public void testVoodooHexAndFear() {
		// Initial checks
		assertEquals(0, source.cardStack().size());
		assertEquals(0, nearTarget.cardStack().size());
		assertEquals(0, farTarget.cardStack().size());

		// Play VOODOO_HEX on source stack
		WorldCard hexCard = new WorldCard(null, VOODOO_HEX);
		source.cardStack().add(new CardPlayedEvent(hexCard, source, null));

		assertEquals(1, source.cardStack().size());

		// Advance time so VOODOO_HEX resolves (50 ticks)
		for (int i = 0; i < 50; i++) {
			source.cardStack().update(gameState.world);
		}

		// After Voodoo Hex finishes resolution, it pops from source's stack and pushes its effect to procChains
		assertEquals(0, source.cardStack().size());
		assertFalse(gameState.world.procChains.isEmpty());

		// Process all procChains in world to apply effects (which adds FEAR to units in range 3)
		while (!gameState.world.procChains.isEmpty()) {
			gameState.world.procChains.removeIf(ProcChain::empty);
			for (ProcChain chain : new ArrayList<>(gameState.world.procChains)) {
				chain.update(gameState.world);
			}
		}

		// Verify targeting results:
		// 1. Source does NOT have FEAR in stack (excludes source)
		assertEquals(0, source.cardStack().size());

		// 2. nearTarget (range 1 <= 3) has exactly 1 FEAR card in stack
		assertEquals(1, nearTarget.cardStack().size());
		assertEquals(FEAR, nearTarget.cardStack().get(0).card().card());

		// 3. farTarget (range 5 > 3) does NOT have FEAR in stack
		assertEquals(0, farTarget.cardStack().size());

		// Now let's verify FEAR card behavior on nearTarget's stack
		// nearTarget initial health is 10
		assertEquals(10, nearTarget.health());

		// Advance time for FEAR card resolution (40 ticks)
		for (int i = 0; i < 40; i++) {
			nearTarget.cardStack().update(gameState.world);
		}

		// After FEAR finishes resolution, it pops from nearTarget's stack and adds effect to procChains
		assertEquals(0, nearTarget.cardStack().size());
		assertFalse(gameState.world.procChains.isEmpty());

		// Process procChains to resolve the damage effect
		while (!gameState.world.procChains.isEmpty()) {
			gameState.world.procChains.removeIf(ProcChain::empty);
			for (ProcChain chain : new ArrayList<>(gameState.world.procChains)) {
				chain.update(gameState.world);
			}
		}

		// Verify nearTarget took 1 damage
		assertEquals(9, nearTarget.health());
	}
}
