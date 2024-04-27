package nomadrealms.game.event;

import nomadrealms.game.GameState;
import nomadrealms.game.world.map.World;

/**
 * These events deterministically dictate the game simulation and must be reconciled during resync. The last 30 frames
 * of InputEvents are stored inside {@link GameState}
 */
public class InputEvent extends SyncedEvent {

    /**
     * Double visitor pattern
     * @param world
     */
    public void resolve(World world) {
        world.resolve(this);
    }

}
