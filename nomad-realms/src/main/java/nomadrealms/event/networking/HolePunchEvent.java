package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.UUID;

@Derializable
public class HolePunchEvent implements SyncedEvent {

	private UUID nonce;

	public HolePunchEvent() {
	}

	public HolePunchEvent(UUID nonce) {
		this.nonce = nonce;
	}

	public UUID nonce() {
		return nonce;
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

}
