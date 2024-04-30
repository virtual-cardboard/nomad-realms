package nomadrealms.game.event;

import nomadrealms.game.GameState;
import nomadrealms.game.world.World;
import nomadrealms.render.ui.GameInterface;

/**
 * These events deterministically dictate the game simulation and must be reconciled during resync. The last 30 frames
 * of InputEvents are stored inside {@link GameState}
 */
public abstract class InputEvent extends SyncedEvent {

    /**
     * Double visitor pattern
     * @param world
     */
    public abstract void resolve(World world);

    /**
     * Double visitor pattern
     * @param ui
     */
    public abstract void resolve(GameInterface ui);

}
