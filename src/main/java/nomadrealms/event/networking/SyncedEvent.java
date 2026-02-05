package nomadrealms.event.networking;

import nomadrealms.context.game.world.World;
import nomadrealms.event.Event;

/**
 * These events must be sent to other players, but do not dictate the game simulation. They are used for other
 * non-time-sensitive events such as chat messages, camera movement, etc.
 */
public interface SyncedEvent extends Event {

    default void link(World world) {
    }

}
