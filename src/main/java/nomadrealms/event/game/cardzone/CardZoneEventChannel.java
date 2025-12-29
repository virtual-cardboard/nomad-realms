package nomadrealms.event.game.cardzone;

import java.util.HashSet;
import java.util.Set;

import nomadrealms.context.game.card.Card;

public class CardZoneEventChannel<T extends Card> {

	protected final Set<CardZoneListener<T>> listeners = new HashSet<>();


	public void send(CardZoneEvent<T> event) {
		for (CardZoneListener<T> listener : listeners) {
			event.handle(listener);
		}
	}

	/**
	 * Subscribe a listener to this event channel.
	 *
	 * @param listener the listener to subscribe
	 */
	public void subscribe(CardZoneListener<T> listener) {
		listeners.add(listener);
	}

	/**
	 * Unsubscribe a listener from this event channel.
	 *
	 * @param listener the listener to unsubscribe
	 */
	public void unsubscribe(CardZoneListener<T> listener) {
		listeners.remove(listener);
	}


}
