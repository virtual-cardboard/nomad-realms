package nomadrealms.context.game.card;

import static nomadrealms.context.game.card.GameCard.ATTACK;
import static nomadrealms.context.game.card.GameCard.PLANNED_PROGRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlannedProgressCardTest {

	private GameState gameState;
	private Farmer source;
	private Farmer target;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(),
				new TemplateGenerationStrategy());
		Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		Tile tile2 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 1));
		source = new Farmer("Source", tile1);
		target = new Farmer("Target", tile2);
		gameState.world.addActor(source, true);
		gameState.world.addActor(target, true);
	}

	@Test
	public void testPlannedProgress_playsNextCardInDeck() {
		// Setup: Source has ATTACK as the next card in their deck
		// Farmer constructor adds cards to deck1, so we clear it first
		source.deckCollection().deck1().clear();
		WorldCard attackCard = new WorldCard(source.deckCollection().deck1(), ATTACK);
		source.deckCollection().deck1().addCard(attackCard);

		assertEquals(1, source.deckCollection().deck1().size());
		// Verify back-reference
		assertEquals(source.deckCollection().deck1(), attackCard.zone());
		assertEquals(0, source.cardStack().size());

		// Play Planned Progress
		// We simulate the effect resolution directly
		WorldCard plannedProgressCard = new WorldCard(source.deckCollection().deck1(), PLANNED_PROGRESS);
		Effect plannedProgressEffect = PLANNED_PROGRESS.expression().effects(gameState.world, null, source, plannedProgressCard).get(0);
		plannedProgressEffect.resolve(gameState.world);

		// After resolution, the ATTACK card should be on the stack
		assertEquals(1, source.cardStack().size());
		assertEquals(ATTACK, source.cardStack().get(0).card().card());

		// The card should be removed from the deck
		assertEquals(0, source.deckCollection().deck1().size());
	}
}
