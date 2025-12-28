package nomadrealms.context.game.zone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.tile.GrayscaleTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardStackTest {

	private CardStack cardStack;
	private CardPlayedEvent event1;
	private CardPlayedEvent event2;

	@BeforeEach
	public void setUp() {
		cardStack = new CardStack();
		event1 = new CardPlayedEvent(null, null, new GrayscaleTile(null, null, 0));
		event2 = new CardPlayedEvent(null, null, new GrayscaleTile(null, null, 0));
	}

	@Test
	public void testAddAndRetrieveCardPlayedEvent() {
		cardStack.addCardPlayedEvent(event1);
		cardStack.addCardPlayedEvent(event2);

		// Test LIFO behavior
		assertEquals(event2, cardStack.getNextCardPlayedEvent());
		cardStack.remove(event2);
		assertEquals(event1, cardStack.getNextCardPlayedEvent());
	}

	@Test
	public void testGetCards() {
		cardStack.addCardPlayedEvent(event1);
		cardStack.addCardPlayedEvent(event2);

		List<CardPlayedEvent> events = cardStack.getCards();
		assertTrue(events.contains(event1));
		assertTrue(events.contains(event2));
		// Test order
		assertEquals(event2, events.get(0));
		assertEquals(event1, events.get(1));
	}

	@Test
	public void testContains() {
		cardStack.addCardPlayedEvent(event1);
		assertTrue(cardStack.contains(event1));
		assertFalse(cardStack.contains(event2));
	}

	@Test
	public void testRemove() {
		cardStack.addCardPlayedEvent(event1);
		cardStack.remove(event1);
		assertFalse(cardStack.contains(event1));
	}

	@Test
	public void testClear() {
		cardStack.addCardPlayedEvent(event1);
		cardStack.addCardPlayedEvent(event2);
		cardStack.clear();
		assertTrue(cardStack.getCards().isEmpty());
	}

	@Test
	public void testGetNextCardPlayedEventOnEmptyStack() {
		assertNull(cardStack.getNextCardPlayedEvent());
	}

}
