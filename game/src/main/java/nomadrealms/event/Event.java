package nomadrealms.event;

/**
 * Marker interface for all events.
 * <p>
 * Every concrete subtype of {@link Event} should also define a {@code public void handle(EventListener listener)}
 * function to allow double-dispatch handling of the event by the listener.
 * <p>
 * <pre>
 *     {@code
 * public void handle(EventListener listener) {
 * 	listener.handle(this);
 * }
 *     }
 * </pre>
 */
public interface Event {

}
