package nomadrealms.game.zone;

import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.zone.CardQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class CardQueueTest {

    private CardQueue cardQueue;
    private CardPlayedEvent event1;
    private CardPlayedEvent event2;

    @BeforeEach
    public void setUp() {
        cardQueue = new CardQueue();
        Tile tile = new Tile(null, null); // Update the constructor call for Tile to match the expected parameters
        event1 = new CardPlayedEvent(null, null, tile);
        event2 = new CardPlayedEvent(null, null, tile);
    }

    @Test
    public void testAddAndRetrieveCardPlayedEvent() {
        cardQueue.addCardPlayedEvent(event1);
        cardQueue.addCardPlayedEvent(event2);

        assertEquals(event1, cardQueue.getNextCardPlayedEvent());
        assertEquals(event2, cardQueue.getNextCardPlayedEvent());
    }

    @Test
    public void testOrderOfCardPlayedEventInQueue() {
        cardQueue.addCardPlayedEvent(event1);
        cardQueue.addCardPlayedEvent(event2);

        Queue<CardPlayedEvent> events = cardQueue.getQueue();
        assertEquals(event1, events.poll());
        assertEquals(event2, events.poll());
    }

    @Test
    public void testGetCards() {
        cardQueue.addCardPlayedEvent(event1);
        cardQueue.addCardPlayedEvent(event2);

        Queue<CardPlayedEvent> events = cardQueue.getQueue(); // Update the getCards() method to return a Queue<CardPlayedEvent> instead of a List<CardPlayedEvent>
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
    }

    @Test
    public void testContains() {
        cardQueue.addCardPlayedEvent(event1);
        assertTrue(cardQueue.contains(event1));
        assertFalse(cardQueue.contains(event2));
    }

    @Test
    public void testRemove() {
        cardQueue.addCardPlayedEvent(event1);
        cardQueue.remove(event1);
        assertFalse(cardQueue.contains(event1));
    }

    @Test
    public void testClear() {
        cardQueue.addCardPlayedEvent(event1);
        cardQueue.addCardPlayedEvent(event2);
        cardQueue.clear();
        assertTrue(cardQueue.getQueue().isEmpty());
    }
}
