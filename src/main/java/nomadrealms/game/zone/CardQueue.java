package nomadrealms.game.zone;

import nomadrealms.game.event.CardPlayedEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CardQueue extends CardZone<CardPlayedEvent> {

    private Queue<CardPlayedEvent> queue;

    public CardQueue() {
        queue = new LinkedList<>();
    }

    public void addCardPlayedEvent(CardPlayedEvent event) {
        queue.add(event);
    }

    public CardPlayedEvent getNextCardPlayedEvent() {
        return queue.poll();
    }

    public List<CardPlayedEvent> getCards() {
        return new LinkedList<>(queue);
    }

    public int size() {
        return queue.size();
    }

    public CardPlayedEvent get(int index) {
        return (CardPlayedEvent) queue.toArray()[index];
    }

    public void add(CardPlayedEvent event) {
        queue.add(event);
    }

    public boolean contains(CardPlayedEvent event) {
        return queue.contains(event);
    }

    public void remove(CardPlayedEvent event) {
        queue.remove(event);
    }

    public void clear() {
        queue.clear();
    }

    public Queue<CardPlayedEvent> getQueue() {
        return queue;
    }
}
