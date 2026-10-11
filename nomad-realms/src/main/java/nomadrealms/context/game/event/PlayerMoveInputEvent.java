package nomadrealms.context.game.event;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.event.networking.SyncedEventHandler;
import nomadrealms.render.ui.custom.game.GameInterface;

@Derializable
public class PlayerMoveInputEvent implements InputEvent {

	private CardPlayer source;
	private TileCoordinate target;

	/**
	 * No-arg constructor for serialization.
	 */
	protected PlayerMoveInputEvent() {
	}

	public PlayerMoveInputEvent(CardPlayer source, TileCoordinate target) {
		this.source = source;
		this.target = target;
	}

	public CardPlayer source() {
		return source;
	}

	public TileCoordinate target() {
		return target;
	}

	@Override
	public void resolve(World world) {
		world.resolve(this);
	}

	@Override
	public void resolve(GameInterface ui) {
		ui.resolve(this);
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

	@Override
	public void reindex(World world) {
		if (source != null) {
			source.reindex(world);
		}
	}

	@Override
	public String toString() {
		return "PlayerMoveInputEvent{" +
				"source=" + source +
				", target=" + target +
				'}';
	}

}
