package nomadrealms.context.game.event;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.world.World;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.event.networking.SyncedEventHandler;
import nomadrealms.render.ui.custom.game.GameInterface;

/**
 * These events deterministically dictate the game simulation and must be reconciled during net-code rollback. The last
 * 30 frames of {@link InputEventFrame InputEventFrames} are stored inside {@link GameState}.
 */
public interface InputEvent extends SyncedEvent {

	@Override
	default void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

	/**
	 * Double visitor pattern
	 * <p>
	 * Lunkle: Actually, double visitor pattern is not required here. There is only ever one type of world.
	 * TODO: modify CardPlayedEvent and DropItemEvent to not be double visitor pattern.
	 * TODO: rename this to "handle" instead of "resolve" since resolve is used for card resolution
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

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	default void reindex(World world) {
	}

}
