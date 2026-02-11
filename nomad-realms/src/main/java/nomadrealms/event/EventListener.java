package nomadrealms.event;

/**
 * Event listener interface for handling events of type T.
 * <p>
 * Each listener must implement the handle methods to process the subclasses of T events.
 *
 * @param <T> The type of events to handle
 * @author nomadrealms
 */
public interface EventListener<T extends Event> {

	public default void handle(T event) {
		throw new UnsupportedOperationException("Handle method not implemented for " + event.getClass().getName());
	}

}
