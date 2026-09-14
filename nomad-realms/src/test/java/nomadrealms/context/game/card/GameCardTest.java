package nomadrealms.context.game.card;

import static nomadrealms.context.game.card.CardKeyword.HEAVY;
import static nomadrealms.context.game.card.CardType.ACTION;
import static nomadrealms.context.game.card.CardType.STRUCTURE;
import static nomadrealms.context.game.card.FixedCards.ATTACK;
import static nomadrealms.context.game.card.FixedCards.CREATE_ROCK;
import static nomadrealms.context.game.card.FixedCards.DASH;
import static nomadrealms.context.game.card.FixedCards.ELECTROSTATIC_ZAPPER;
import static nomadrealms.context.game.card.FixedCards.ICE_CUBE;
import static nomadrealms.context.game.card.FixedCards.REST;
import static nomadrealms.context.game.card.FixedCards.WOODEN_CHEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import nomadrealms.context.game.zone.CardStack;
import org.junit.jupiter.api.Test;

public class GameCardTest {

	@Test
	public void testCardTypes() {
		assertEquals(ACTION, DASH.card().type());
		assertEquals(ACTION, ATTACK.card().type());
		assertEquals(STRUCTURE, CREATE_ROCK.card().type());
		assertEquals(STRUCTURE, ELECTROSTATIC_ZAPPER.card().type());
		assertEquals(STRUCTURE, WOODEN_CHEST.card().type());
		assertEquals(ACTION, REST.card().type());
	}

	@Test
	public void testKeywords() {
		GameCard cardWithoutKeywords = ATTACK.card();
		assertTrue(cardWithoutKeywords.keywords().isEmpty());

		GameCard heavyCard = ICE_CUBE.card().keywords(HEAVY);
		assertTrue(heavyCard.keywords().contains(HEAVY));

		WorldCard worldCardWithoutKeywords = new WorldCard(null, cardWithoutKeywords);
		assertTrue(worldCardWithoutKeywords.keywords().isEmpty());

		WorldCard worldHeavyCard = new WorldCard(null, heavyCard);
		assertTrue(worldHeavyCard.keywords().contains(HEAVY));
	}

	@Test
	public void testHeavyCardFallsToBottomOnUpdate() {
		GameCard heavyCard = ICE_CUBE.card().keywords(HEAVY);
		GameCard normalCard = ATTACK.card();

		GameState gameState = new GameState("Test World", new LinkedList<>(), new TemplateGenerationStrategy());
		Farmer player = new Farmer("Farmer", gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0)));

		WorldCard heavyWorldCard = new WorldCard(null, heavyCard);
		WorldCard normalWorldCard = new WorldCard(null, normalCard);

		CardPlayedEvent heavyEvent = new CardPlayedEvent(heavyWorldCard, player, null);
		CardPlayedEvent normalEvent = new CardPlayedEvent(normalWorldCard, player, null);

		CardStack stack = new CardStack();
		stack.add(normalEvent);
		stack.add(heavyEvent);

		assertEquals(heavyEvent, stack.top().event());

		stack.update(gameState.world);

		assertEquals(normalEvent, stack.top().event());
		assertEquals(heavyEvent, stack.get(0));
	}

}
