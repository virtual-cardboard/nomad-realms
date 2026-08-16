package nomadrealms.context.game.card;

import static java.util.Collections.singletonList;
import static nomadrealms.context.game.card.GameCard.DEBILITATING_FEAR;
import static nomadrealms.context.game.card.GameCard.FEAR;
import static nomadrealms.context.game.card.GameCard.MOVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.condition.HasCardInZoneCondition;
import nomadrealms.context.game.card.query.zone.CardZoneQuery;
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

public class DebilitatingFearCardTest {

	private GameState gameState;
	private Farmer source;
	private Farmer targetWithFear;
	private Farmer targetWithoutFear;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(),
				new TemplateGenerationStrategy());

		Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		Tile tile2 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 1));
		Tile tile3 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 2));

		source = new Farmer("Source", tile1);
		targetWithFear = new Farmer("TargetWithFear", tile2);
		targetWithoutFear = new Farmer("TargetWithoutFear", tile3);

		gameState.world.addActor(source, true);
		gameState.world.addActor(targetWithFear, true);
		gameState.world.addActor(targetWithoutFear, true);
	}

	@Test
	public void testCardProperties() {
		assertEquals("Debilitating Fear", DEBILITATING_FEAR.title());
		assertEquals(CardType.ACTION, DEBILITATING_FEAR.type());
		assertEquals(10, DEBILITATING_FEAR.manaCost());
		assertEquals(30, DEBILITATING_FEAR.resolutionTime());
	}

	@Test
	public void testTargetingCondition() {
		HasCardInZoneCondition condition = new HasCardInZoneCondition(
				CardZoneQuery.cardZone(
						context -> singletonList((Farmer) context.target())
				),
				FEAR);

		// Event source is 'source', target is 'targetWithFear'
		targetWithFear.cardStack().add(new CardPlayedEvent(new WorldCard(null, FEAR), source, targetWithFear));
		targetWithoutFear.cardStack().add(new CardPlayedEvent(new WorldCard(null, MOVE), source, targetWithoutFear));

		assertTrue(condition.test(gameState.world, targetWithFear, source));
		assertFalse(condition.test(gameState.world, targetWithoutFear, source));
	}

	@Test
	public void testDebilitatingFearClearsStack() {
		// Add MOVE (played by target) and FEAR (played by source) to targetWithFear stack
		targetWithFear.cardStack().add(new CardPlayedEvent(new WorldCard(null, MOVE), targetWithFear, null));
		targetWithFear.cardStack().add(new CardPlayedEvent(new WorldCard(null, FEAR), source, targetWithFear));
		assertEquals(2, targetWithFear.cardStack().size());

		// Play DEBILITATING_FEAR targeting targetWithFear
		WorldCard debCard = new WorldCard(null, DEBILITATING_FEAR);
		source.cardStack().add(new CardPlayedEvent(debCard, source, targetWithFear));

		// Advance 30 ticks for DEBILITATING_FEAR to resolve
		for (int i = 0; i < 30; i++) {
			source.cardStack().update(gameState.world);
		}

		// Verify DEBILITATING_FEAR popped from source stack and pushed effect to procChains
		assertEquals(0, source.cardStack().size());
		assertFalse(gameState.world.procChains.isEmpty());

		// Process procChains
		while (!gameState.world.procChains.isEmpty()) {
			gameState.world.procChains.removeIf(ProcChain::empty);
			for (ProcChain chain : new ArrayList<>(gameState.world.procChains)) {
				chain.update(gameState.world);
			}
		}

		// Target stack should now be cleared
		assertEquals(0, targetWithFear.cardStack().size());
	}
}
