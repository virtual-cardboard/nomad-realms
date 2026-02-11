package nomadrealms.event;

import java.util.HashSet;
import java.util.Set;

public class EventChannel<T extends Event> {

	protected final Set<EventListener<T>> listeners = new HashSet<>();

	/**
	 * Subscribe a listener to this event channel.
	 *
	 * @param listener the listener to subscribe
	 */
	public void subscribe(EventListener<T> listener) {
		listeners.add(listener);
	}

	/**
	 * Unsubscribe a listener from this event channel.
	 *
	 * @param listener the listener to unsubscribe
	 */
	public void unsubscribe(EventListener<T> listener) {
		listeners.remove(listener);
	}


}
