package nomadrealms.context.game.card;

import static nomadrealms.context.game.card.GameCard.MIND_BLAST;
import static nomadrealms.context.game.card.GameCard.ATTACK;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.ArrayList;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.event.ProcChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MindBlastCardTest {

	private GameState gameState;
	private Farmer source;
	private Farmer target;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(),
				new OverworldGenerationStrategy(123));
		// Use forced=true to overwrite any existing actors (like trees)
		Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		Tile tile2 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 1));
		source = new Farmer("Source", tile1);
		target = new Farmer("Target", tile2);
		gameState.world.addActor(source, true);
		gameState.world.addActor(target, true);
	}

	@Test
	public void testMindBlast_damageBasedOnStackSize() {
		// Setup: Source has 2 cards in stack (ATTACK, ATTACK)
		WorldCard card1 = new WorldCard(ATTACK);
		WorldCard card2 = new WorldCard(ATTACK);

		source.cardStack().add(new CardPlayedEvent(card1, source, target));
		source.cardStack().add(new CardPlayedEvent(card2, source, target));

		// Verify initial stack size
		assertEquals(2, source.cardStack().size());

		// Play Mind Blast
		// We simulate the effect resolution directly to see if the stack size query works correctly
		// However, the query relies on source.cardStack().size()

		// If we just resolve the effect:
		Effect mindBlastEffect = MIND_BLAST.expression().effects(gameState.world, target, source).get(0);
		mindBlastEffect.resolve(gameState.world);

		// Damage should be 2 because there are 2 cards in stack
		assertEquals(8, target.health()); // Started at 10, took 2 damage
	}

	@Test
	public void testMindBlast_stackProcessing() {
		// This test tries to simulate the "pop then resolve" behavior.

		// Setup: Source has 2 cards in stack (ATTACK, MIND_BLAST)
		WorldCard card1 = new WorldCard(ATTACK);
		WorldCard card2 = new WorldCard(MIND_BLAST);

		source.cardStack().add(new CardPlayedEvent(card1, source, target));
		// Add Mind Blast to stack
		source.cardStack().add(new CardPlayedEvent(card2, source, target));

		assertEquals(2, source.cardStack().size());

		// Simulate update cycle where Mind Blast is processed
		// We need to force the top card to be ready.
		// CardStack.update() checks if top().isReady().

		// Let's manually manipulate the stack entry to be ready
		source.cardStack().top().setCounter(MIND_BLAST.resolutionTime());

		// Call update on stack
		source.cardStack().update(gameState.world);

		// After update:
		// 1. Mind Blast should be popped (stack size 1)
		// 2. Effect should be resolved (added to procChains)
		// 3. ProcChain should execute

		// Check stack size
		assertEquals(1, source.cardStack().size());

		// Process proc chains in world
		// Loop enough times to ensure all effects in the chain are resolved
		// Typically chain has start, damage, end (3 effects)
		for (int i = 0; i < 10 && !gameState.world.procChains.isEmpty(); i++) {
			gameState.world.procChains.removeIf(ProcChain::empty);
			for (ProcChain chain : new ArrayList<>(gameState.world.procChains)) {
				chain.update(gameState.world);
			}
		}

		// Damage should be 1 (because only 1 card remained in stack when Mind Blast resolved)
		assertEquals(9, target.health()); // Started at 10, took 1 damage
	}
}
