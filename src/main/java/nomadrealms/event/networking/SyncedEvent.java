package nomadrealms.event.networking;

import engine.serialization.Derializable;
import nomadrealms.event.Event;

/**
 * These events must be sent to other players, but do not dictate the game simulation. They are used for other
 * non-time-sensitive events such as chat messages, camera movement, etc.
 */
@Derializable
public interface SyncedEvent extends Event {
}
