package nomadrealms.game.event;

import nomadrealms.game.GameState;
import nomadrealms.game.world.World;
import nomadrealms.render.ui.custom.GameInterface;

/**
 * These events deterministically dictate the game simulation and must be reconciled during net-code rollback. The last
 * 30 frames of {@link InputEventFrame InputEventFrames} are stored inside {@link GameState}.
 */
public interface InputEvent extends SyncedEvent {

	/**
	 * Double visitor pattern
	 * <p>
	 * Lunkle: Actually, double visitor pattern is not required here. There is only ever one type of world.
	 * TODO: modify CardPlayedEvent and DropItemEvent to not be double visitor pattern.
	 *
	 * @param world
	 */
	void resolve(World world);

	/**
	 * Double visitor pattern will often be used here due to different UIs performing different actions depending on the
	 * InputEvent.
	 *
	 * @param ui
	 */
	void resolve(GameInterface ui);

}
