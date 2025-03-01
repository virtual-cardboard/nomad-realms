package nomadrealms.game.event;

import nomadrealms.game.GameState;
import nomadrealms.game.world.World;
import nomadrealms.render.ui.GameInterface;

/**
 * These events deterministically dictate the game simulation and must be reconciled during net-code rollback. The last
 * 30 frames of {@link InputEventFrame InputEventFrames} are stored inside {@link GameState}.
 */
public interface InputEvent extends SyncedEvent {

	/**
	 * Double visitor pattern
	 *
	 * @param world
	 */
	void resolve(World world);

	/**
	 * Double visitor pattern
	 *
	 * @param ui
	 */
	void resolve(GameInterface ui);

}
