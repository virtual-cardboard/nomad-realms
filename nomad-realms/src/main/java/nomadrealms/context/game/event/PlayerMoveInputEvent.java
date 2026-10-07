package nomadrealms.context.game.event;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.networking.SyncedEventHandler;
import nomadrealms.render.ui.custom.game.GameInterface;

@Derializable
public class PlayerMoveInputEvent implements InputEvent {

	private CardPlayer source;
	private Tile targetTile;

	protected PlayerMoveInputEvent() {
	}

	public PlayerMoveInputEvent(CardPlayer source, Tile targetTile) {
		this.source = source;
		this.targetTile = targetTile;
	}

	public CardPlayer source() {
		return source;
	}

	public Tile targetTile() {
		return targetTile;
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
	public String toString() {
		return "PlayerMoveInputEvent{" +
				"source=" + source +
				", targetTile=" + targetTile +
				'}';
	}
}
