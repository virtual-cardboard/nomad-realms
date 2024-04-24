package nomadrealms.game.event;

import nomadrealms.game.GameState;

/**
 * These events deterministically dictate the game simulation and must be reconciled during resync. The last 30 frames
 * of InputEvents are stored inside {@link GameState}
 */
public class InputEvent extends SyncedEvent {
}
