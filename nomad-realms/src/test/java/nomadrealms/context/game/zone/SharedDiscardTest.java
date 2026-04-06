package nomadrealms.context.game.zone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.WorldCard;
import org.junit.jupiter.api.Test;

public class SharedDiscardTest {

	@Test
	public void testSharedDiscardRestock() {
		DeckCollection collection = new DeckCollection();
		Deck deck1 = collection.deck1();
		Deck deck2 = collection.deck2();

		WorldCard card1 = new WorldCard(deck1, GameCard.DASH);
		WorldCard card2 = new WorldCard(deck2, GameCard.ATTACK);

		collection.discardZone().addCard(card1);
		collection.discardZone().addCard(card2);

		assertEquals(2, collection.discardZone().size());
		assertEquals(0, deck1.size());
		assertEquals(0, deck2.size());

		deck1.restock(collection.discardZone());

		assertEquals(1, deck1.size());
		assertEquals(card1, deck1.peek());
		assertEquals(1, collection.discardZone().size());
		assertTrue(collection.discardZone().getCards().contains(card2));
		assertFalse(collection.discardZone().getCards().contains(card1));

		deck2.restock(collection.discardZone());

		assertEquals(1, deck2.size());
		assertEquals(card2, deck2.peek());
		assertEquals(0, collection.discardZone().size());
	}
}
