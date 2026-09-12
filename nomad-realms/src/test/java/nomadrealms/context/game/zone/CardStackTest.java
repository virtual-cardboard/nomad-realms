package nomadrealms.context.game.zone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import org.junit.jupiter.api.Test;

public class CardStackTest {

	@Test
	public void testCardStackOperations() {
		CardStack stack = new CardStack();
		assertTrue(stack.getCards().isEmpty());
		assertNull(stack.top());
		assertNull(stack.pop());

		WorldCard card1 = new WorldCard(null, GameCard.ATTACK);
		CardPlayedEvent event1 = new CardPlayedEvent(card1, null, null);
		stack.add(event1);

		assertEquals(1, stack.size());
		assertNotNull(stack.top());
		assertEquals(event1, stack.top().event());
		assertTrue(stack.contains(event1));

		WorldCard card2 = new WorldCard(null, GameCard.DASH);
		CardPlayedEvent event2 = new CardPlayedEvent(card2, null, null);
		stack.add(event2);

		assertEquals(2, stack.size());
		assertEquals(event2, stack.top().event());

		CardStackEntry popped = stack.pop();
		assertEquals(event2, popped.event());
		assertEquals(1, stack.size());
		assertEquals(event1, stack.top().event());
	}

}
